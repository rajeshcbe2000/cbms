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
package com.see.truetransact.ui.termloan.kcc.multiplerenewal;

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
public class KCCMultipleRenewalUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private KCCMultipleRenewalOB observable;
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

    /**
     * Creates new form AuthorizeUI
     */
    public KCCMultipleRenewalUI() {
        setupInit();
        setupScreen();
        //tblData.setAutoCreateRowSorter(true);
        ClientUtil.enableDisable(panAward, true);
        panAward.setVisible(true);
        cboProdType.setModel(observable.getCbmProdId());
    }

    /**
     * Creates new form AuthorizeUI
     */
    public KCCMultipleRenewalUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        panAward.setVisible(false);
        ClientUtil.disableAll(panAward, false);
        cboProdType.setModel(observable.getCbmProdId());
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
        txtNoOfYears.setAllowAll(true);
        chkAllAccounts.setEnabled(true);
        chkDueOn.setEnabled(true);
        chkNoOfYears.setEnabled(true);
        chkRenewUpTo.setEnabled(true);
        tdtRenewFrom.setEnabled(false);
        txtRenewalYears.setEnabled(false);
        tdtDueOnDt.setEnabled(false);
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
            observable = new KCCMultipleRenewalOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
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
        int rowIndexSelected = tblData.getSelectedRow();
        if (previousRow != -1) {
            if (((Boolean) tblData.getValueAt(previousRow, 0)).booleanValue()) {
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
        int rowIndexSelected = tblData.getSelectedRow();
        if (!((Boolean) tblData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
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
        tdtDueOnDt = new com.see.truetransact.uicomponent.CDateField();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        chkDueOn = new com.see.truetransact.uicomponent.CCheckBox();
        chkAllAccounts = new com.see.truetransact.uicomponent.CCheckBox();
        panAward = new com.see.truetransact.uicomponent.CPanel();
        txtNoOfYears = new com.see.truetransact.uicomponent.CTextField();
        tdtRenewFrom = new com.see.truetransact.uicomponent.CDateField();
        chkNoOfYears = new com.see.truetransact.uicomponent.CCheckBox();
        chkRenewUpTo = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtRenewalYears = new com.see.truetransact.uicomponent.CTextField();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblToDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        chkUnselectSurety = new com.see.truetransact.uicomponent.CCheckBox();
        panRiskFund = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch2 = new com.see.truetransact.uicomponent.CPanel();
        panArbit1 = new com.see.truetransact.uicomponent.CPanel();
        lblProdType1 = new com.see.truetransact.uicomponent.CLabel();
        btnShowRenewedAccts = new com.see.truetransact.uicomponent.CButton();
        btnRenewalIdSearch = new com.see.truetransact.uicomponent.CButton();
        txtRenewalId = new com.see.truetransact.uicomponent.CTextField();
        lblProdDescription = new com.see.truetransact.uicomponent.CLabel();
        panRiskFundTable = new com.see.truetransact.uicomponent.CPanel();
        chkRiskFundSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcRiskFundTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblRiskFundData = new com.see.truetransact.uicomponent.CTable();
        lblRiskFundAccts = new com.see.truetransact.uicomponent.CLabel();
        lblRiskFundNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblRiskFundNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panRiskFundTotDetails = new com.see.truetransact.uicomponent.CPanel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblRiskFundSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRiskFundVal = new com.see.truetransact.uicomponent.CLabel();
        cLabel8 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTaxVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch1 = new com.see.truetransact.uicomponent.CPanel();
        btnRiskFundProcess = new com.see.truetransact.uicomponent.CButton();
        btnRiskFundClose = new com.see.truetransact.uicomponent.CButton();
        btnRiskFundClear = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("KCC Multiple Renewal");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));

        panKCCREnewal.setMinimumSize(new java.awt.Dimension(800, 35));
        panKCCREnewal.setPreferredSize(new java.awt.Dimension(800, 35));

        cLabel1.setText("Selected Records:");

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 150));

        panMultiSearch1.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panMultiSearch1.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setMinimumSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setPreferredSize(new java.awt.Dimension(450, 150));

        panArbit.setMinimumSize(new java.awt.Dimension(400, 65));
        panArbit.setPreferredSize(new java.awt.Dimension(400, 65));

        lblProdType.setText("Product Type");

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(160);

        chkDueOn.setText("Due On");
        chkDueOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDueOnActionPerformed(evt);
            }
        });

        chkAllAccounts.setText("All Accounts");
        chkAllAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAllAccountsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panArbitLayout = new javax.swing.GroupLayout(panArbit);
        panArbit.setLayout(panArbitLayout);
        panArbitLayout.setHorizontalGroup(
            panArbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArbitLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panArbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkDueOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkAllAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panArbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tdtDueOnDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProdType, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panArbitLayout.setVerticalGroup(
            panArbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArbitLayout.createSequentialGroup()
                .addGap(0, 7, Short.MAX_VALUE)
                .addGroup(panArbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panArbitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panArbitLayout.createSequentialGroup()
                        .addComponent(chkAllAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkDueOn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtDueOnDt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panMultiSearch1Layout = new javax.swing.GroupLayout(panMultiSearch1);
        panMultiSearch1.setLayout(panMultiSearch1Layout);
        panMultiSearch1Layout.setHorizontalGroup(
            panMultiSearch1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMultiSearch1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panArbit, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panMultiSearch1Layout.setVerticalGroup(
            panMultiSearch1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMultiSearch1Layout.createSequentialGroup()
                .addComponent(panArbit, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panAward.setBorder(javax.swing.BorderFactory.createTitledBorder("Renewal Details"));
        panAward.setMaximumSize(new java.awt.Dimension(250, 150));
        panAward.setMinimumSize(new java.awt.Dimension(250, 150));
        panAward.setPreferredSize(new java.awt.Dimension(250, 150));

        chkNoOfYears.setText("No Of Years");
        chkNoOfYears.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNoOfYearsActionPerformed(evt);
            }
        });

        chkRenewUpTo.setText("Renew From");
        chkRenewUpTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRenewUpToActionPerformed(evt);
            }
        });

        cLabel3.setText("Renewal Years");

        txtRenewalYears.setAllowNumber(true);

        javax.swing.GroupLayout panAwardLayout = new javax.swing.GroupLayout(panAward);
        panAward.setLayout(panAwardLayout);
        panAwardLayout.setHorizontalGroup(
            panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAwardLayout.createSequentialGroup()
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkRenewUpTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkNoOfYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAwardLayout.createSequentialGroup()
                        .addComponent(txtNoOfYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panAwardLayout.createSequentialGroup()
                        .addComponent(tdtRenewFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRenewalYears, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        panAwardLayout.setVerticalGroup(
            panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAwardLayout.createSequentialGroup()
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoOfYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkNoOfYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkRenewUpTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tdtRenewFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtRenewalYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnProcess.setText("Show ");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panSearchConditionLayout = new javax.swing.GroupLayout(panSearchCondition);
        panSearchCondition.setLayout(panSearchConditionLayout);
        panSearchConditionLayout.setHorizontalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(panMultiSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panAward, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panSearchConditionLayout.setVerticalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addGap(0, 5, Short.MAX_VALUE)
                        .addComponent(panAward, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addComponent(panMultiSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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

        tblData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        lblToDate1.setText("Loan Account Holders List");
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
        btnSave.setText("Renew");
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

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));

        chkUnselectSurety.setForeground(new java.awt.Color(0, 0, 204));
        chkUnselectSurety.setText("Exclude the Accounts not having enough security value");
        chkUnselectSurety.setFont(new java.awt.Font("MS Sans Serif", 1, 12)); // NOI18N
        chkUnselectSurety.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUnselectSuretyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panKCCREnewalLayout = new javax.swing.GroupLayout(panKCCREnewal);
        panKCCREnewal.setLayout(panKCCREnewalLayout);
        panKCCREnewalLayout.setHorizontalGroup(
            panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panKCCREnewalLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(chkUnselectSurety, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panKCCREnewalLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 739, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panKCCREnewalLayout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(31, Short.MAX_VALUE)))
        );
        panKCCREnewalLayout.setVerticalGroup(
            panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panKCCREnewalLayout.createSequentialGroup()
                .addContainerGap(486, Short.MAX_VALUE)
                .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkUnselectSurety, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSelectedRecords, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(58, 58, 58))
            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panKCCREnewalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(43, 43, 43)
                    .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(10, Short.MAX_VALUE)))
        );

        cTabbedPane1.addTab("KCC Rnewal", panKCCREnewal);

        panMultiSearch2.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panMultiSearch2.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch2.setMinimumSize(new java.awt.Dimension(450, 150));

        panArbit1.setMinimumSize(new java.awt.Dimension(400, 65));

        lblProdType1.setText("Renewal Id");

        btnShowRenewedAccts.setText("Show");
        btnShowRenewedAccts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowRenewedAcctsActionPerformed(evt);
            }
        });

        btnRenewalIdSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRenewalIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewalIdSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panArbit1Layout = new javax.swing.GroupLayout(panArbit1);
        panArbit1.setLayout(panArbit1Layout);
        panArbit1Layout.setHorizontalGroup(
            panArbit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArbit1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProdType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtRenewalId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRenewalIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnShowRenewedAccts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblProdDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        panArbit1Layout.setVerticalGroup(
            panArbit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panArbit1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panArbit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRenewalIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panArbit1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnShowRenewedAccts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtRenewalId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProdType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblProdDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout panMultiSearch2Layout = new javax.swing.GroupLayout(panMultiSearch2);
        panMultiSearch2.setLayout(panMultiSearch2Layout);
        panMultiSearch2Layout.setHorizontalGroup(
            panMultiSearch2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMultiSearch2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panArbit1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panMultiSearch2Layout.setVerticalGroup(
            panMultiSearch2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMultiSearch2Layout.createSequentialGroup()
                .addComponent(panArbit1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

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

        tblRiskFundData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRiskFundData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRiskFundData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblRiskFundData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblRiskFundData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRiskFundDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblRiskFundDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblRiskFundDataMouseReleased(evt);
            }
        });
        tblRiskFundData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblRiskFundDataMouseMoved(evt);
            }
        });
        srcRiskFundTable.setViewportView(tblRiskFundData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRiskFundTable.add(srcRiskFundTable, gridBagConstraints);

        lblRiskFundAccts.setText("Loan Account Holders List");
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

        lblRiskFundNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblRiskFundNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblRiskFundNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRiskFundTable.add(lblRiskFundNoOfRecordsVal, gridBagConstraints);

        cLabel2.setText("Selected Records");

        cLabel4.setText("Risk Fund");

        cLabel8.setText("GST ");

        javax.swing.GroupLayout panRiskFundTotDetailsLayout = new javax.swing.GroupLayout(panRiskFundTotDetails);
        panRiskFundTotDetails.setLayout(panRiskFundTotDetailsLayout);
        panRiskFundTotDetailsLayout.setHorizontalGroup(
            panRiskFundTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundTotDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblRiskFundSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalRiskFundVal, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(cLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTotalTaxVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        panRiskFundTotDetailsLayout.setVerticalGroup(
            panRiskFundTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundTotDetailsLayout.createSequentialGroup()
                .addGroup(panRiskFundTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalRiskFundVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalTaxVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRiskFundSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        panSearch1.setLayout(new java.awt.GridBagLayout());

        btnRiskFundProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnRiskFundProcess.setText("Process");
        btnRiskFundProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRiskFundProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch1.add(btnRiskFundProcess, gridBagConstraints);

        btnRiskFundClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnRiskFundClose.setText("Close");
        btnRiskFundClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRiskFundCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch1.add(btnRiskFundClose, gridBagConstraints);

        btnRiskFundClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnRiskFundClear.setText("Clear");
        btnRiskFundClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRiskFundClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnRiskFundClear, gridBagConstraints);

        javax.swing.GroupLayout panRiskFundLayout = new javax.swing.GroupLayout(panRiskFund);
        panRiskFund.setLayout(panRiskFundLayout);
        panRiskFundLayout.setHorizontalGroup(
            panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundLayout.createSequentialGroup()
                .addGroup(panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(panMultiSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(panRiskFundTotDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panRiskFundLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(panRiskFundTable, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );
        panRiskFundLayout.setVerticalGroup(
            panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(panMultiSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(panRiskFundTotDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panRiskFundLayout.createSequentialGroup()
                    .addGap(122, 122, 122)
                    .addComponent(panRiskFundTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(96, Short.MAX_VALUE)))
        );

        cTabbedPane1.addTab("Risk Fund Processing", panRiskFund);

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

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(panKCCREnewal);
        isEdit = false;
        btnSave.setEnabled(true);
        lblNoOfRecordsVal.setText("");
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*
                 * (evt.getClickCount() == 2) &&
                 */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
        }
    }//GEN-LAST:event_tblDataMouseReleased
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (viewType.equalsIgnoreCase("RENEWAL_ID")) {
            if (hash.containsKey("RENEWAL_ID") && hash.get("RENEWAL_ID") != null) {
                txtRenewalId.setText(CommonUtil.convertObjToStr(hash.get("RENEWAL_ID")));
                observable.setRiskFundProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                observable.setRiskFundProdDesc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                lblProdDescription.setText(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
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

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        Point p = evt.getPoint();
        String tip =
                String.valueOf(
                tblData.getModel().getValueAt(
                tblData.rowAtPoint(p),
                tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
//        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(), 1));
//
//            TableDialogUI tableData = new TableDialogUI("getNoticeChargeDetails", whereMap);
//            tableData.setTitle("Notice Sent Details for " + tblData.getValueAt(tblData.getSelectedRow(), 1));
//            tableData.setPreferredSize(new Dimension(750, 450));
//            tableData.show();
//        }       
    }//GEN-LAST:event_tblDataMousePressed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        for (int i = 0; i < tblData.getRowCount(); i++) {
            if (chkAllAccounts.isSelected()) {
                if (dueColourList.contains(String.valueOf(i))) {
                    // tblData.setValueAt(false, i, 0);
                }
            }
            if (suretyColourList.contains(String.valueOf(i))) {
                //tblData.setValueAt(false, i, 0);
            }
             if (deathMarkedCustomerList.contains(String.valueOf(i))) {
                tblData.setValueAt(false, i, 0);
            }
        }
        setSelectedRecord();
        calTotalAmount();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.removeRowsFromGuarantorTable(tblData);
        //  observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    //Added By Suresh
    private void setSelectedRecord() {
        int count = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
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
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        System.out.println("chkng");
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getAccountsForKCCMultipleRenewal");
            if (tdtDueOnDt.getDateValue() != null && tdtDueOnDt.getDateValue().length() > 0) {
                whereMap.put("ARB_FROMDT", getProperDate(DateUtil.getDateMMDDYYYY(tdtDueOnDt.getDateValue())));
            }
            whereMap.put("PROD_ID", CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
            if (chkDueOn.isSelected()) {
                whereMap.put("DUE_DT", DateUtil.getDateMMDDYYYY(tdtDueOnDt.getDateValue()));
                whereMap.put("DUE_ON_DATE", "DUE_ON_DATE");
            }
            if (chkAllAccounts.isSelected()) {
                whereMap.put("CURR_DT", currDt);
                whereMap.put("AS_ON_DT", "AS_ON_DT");
            }
            if (chkNoOfYears.isSelected()) {
                whereMap.put("NO_OF_YERAS", txtNoOfYears.getText());
            } else {
                whereMap.put("RENEW_NO_OF_YERAS", txtRenewalYears.getText());
                whereMap.put("RENEW_FROM_DT", DateUtil.getDateMMDDYYYY(tdtRenewFrom.getDateValue()));
            }

            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateData(viewMap, tblData);
                setColorLists();
                setColour();
                panAward.setVisible(true);
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
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
            whereMap.put("RENEWAL_ID", txtRenewalId.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getAllAccountsByKCCRenewalId");
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            whereMap.put("CHARGE_LIST", chargeList);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateRiskFundData(viewMap, tblRiskFundData);
                setColorListsForRiskFundProcess();
                setColourForRiskFundProcess();
                lblRiskFundNoOfRecordsVal.setText(String.valueOf(tblRiskFundData.getRowCount()));
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
        tblRiskFundData.setDefaultRenderer(Object.class, renderer);
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
        tblData.setDefaultRenderer(Object.class, renderer);
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
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                double limit = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5).toString()).doubleValue();
                double suretyAmt = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7).toString()).doubleValue();
                if (suretyAmt < limit) {
                    suretyColourList.add(String.valueOf(i));
                }
                if (chkAllAccounts.isSelected()) {
                    Date dueDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblData.getValueAt(i, 4).toString()));
                    if (DateUtil.dateDiff(dueDt, currDt) > 0) {
                        dueColourList.add(String.valueOf(i));
                    }
                }
                String acctNum = CommonUtil.convertObjToStr(tblData.getValueAt(i, 1).toString());
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
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblRiskFundData.getRowCount(); i++) {
                double availableBal = CommonUtil.convertObjToDouble(tblRiskFundData.getValueAt(i, 5).toString()).doubleValue();
                double totalAmount = CommonUtil.convertObjToDouble(tblRiskFundData.getValueAt(i, 10).toString()).doubleValue();
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
            observable.removeRowsFromGuarantorTable(tblData);
            lblNoOfRecordsVal.setText("");
            //observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here: 

        //  generateNotice();
        ArrayList lstTotRenew = new ArrayList();
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                HashMap renewAcctMap = new HashMap();
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    for (int j = 1; j < tblData.getColumnCount(); j++) {
                        renewAcctMap.put("ACCT_NUM", tblData.getValueAt(i, 1));
                        renewAcctMap.put("BORROW_NO", tblData.getValueAt(i, 2));
                        renewAcctMap.put("FROM_DT", tblData.getValueAt(i, 3));
                        renewAcctMap.put("TO_DT", tblData.getValueAt(i, 4));
                        renewAcctMap.put("LIMIT", tblData.getValueAt(i, 5));
                        renewAcctMap.put("AVAILABLE_BALANCE", tblData.getValueAt(i, 6));
                        renewAcctMap.put("SURETY_AMOUNT", tblData.getValueAt(i, 7));
                        renewAcctMap.put("RENEW_FROM_DT", tblData.getValueAt(i, 8));
                        renewAcctMap.put("RENEW_TO_DT", tblData.getValueAt(i, 9));
                    }
                    lstTotRenew.add(renewAcctMap);
                }
            }
        }

        HashMap arbMap = new HashMap();
        arbMap.put("KCC_RENEWAL", lstTotRenew);
        HashMap resultMap = observable.renewAccounts(arbMap);
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            String renewId = "";
            if (resultMap != null && resultMap.containsKey("RENEWAL_ID") && resultMap.get("RENEWAL_ID") != null) {
                renewId = CommonUtil.convertObjToStr(resultMap.get("RENEWAL_ID"));
            }
            ClientUtil.showMessageWindow("Renewal Completed . Id : " + renewId + "\n");
            observable.removeRowsFromGuarantorTable(tblData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
        }
//        if (btnPostCharges.isEnabled()) {
//            int confirm = ClientUtil.confirmationAlert("Postage Charges Not Yet Applied" + "\n" + "Do you want to Apply");
//            if (confirm == 0) {
//                btnPostChargesActionPerformed(null);
//            }
//        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() == 0) {
            ClientUtil.displayAlert("Select a product !!");
        } else if (!chkAllAccounts.isSelected() && !chkDueOn.isSelected()) {
            ClientUtil.displayAlert("Select either all acounts or due on !!");
        } else if (chkDueOn.isSelected() && tdtDueOnDt.getDateValue().length() <= 0) {
            ClientUtil.displayAlert("Select due date !!");
        } else if (!chkNoOfYears.isSelected() && !chkRenewUpTo.isSelected()) {
            ClientUtil.displayAlert("Select either number of years or renew up to date !!");
        } else if (chkNoOfYears.isSelected() && txtNoOfYears.getText().length() <= 0) {
            ClientUtil.displayAlert("Enter number of years !!");
        } else if (chkRenewUpTo.isSelected() && tdtRenewFrom.getDateValue().length() <= 0) {
            ClientUtil.displayAlert("Select renew up to date !!");
        } else if (chkRenewUpTo.isSelected() && txtRenewalYears.getText().length() <= 0) {
            ClientUtil.displayAlert("Enter the number of yeass to be renewed from date!");
        } else {
            lblNoOfRecordsVal.setText("");
            isEdit = false;
            btnSave.setEnabled(true);
            populateData();
            tblData.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblData.getColumnModel().getColumn(7).setPreferredWidth(100);
            tblData.getColumnModel().getColumn(8).setPreferredWidth(100);
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void chkAllAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAllAccountsActionPerformed
        // TODO add your handling code here:
        if (chkAllAccounts.isSelected()) {
            chkDueOn.setSelected(false);
            tdtDueOnDt.setEnabled(false);
        } else {
            chkDueOn.setSelected(true);
            tdtDueOnDt.setEnabled(false);
        }
    }//GEN-LAST:event_chkAllAccountsActionPerformed

    private void chkDueOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDueOnActionPerformed
        // TODO add your handling code here:
        if (chkDueOn.isSelected()) {
            chkAllAccounts.setSelected(false);
            tdtDueOnDt.setEnabled(true);
        } else {
            chkAllAccounts.setSelected(true);
        }
    }//GEN-LAST:event_chkDueOnActionPerformed

    private void chkNoOfYearsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNoOfYearsActionPerformed
        // TODO add your handling code here:
        if (chkNoOfYears.isSelected()) {
            txtNoOfYears.setEnabled(true);
            chkRenewUpTo.setSelected(false);
            tdtRenewFrom.setEnabled(false);
            txtRenewalYears.setEnabled(false);
        } else {
            chkRenewUpTo.setSelected(true);
            tdtRenewFrom.setEnabled(true);
            txtRenewalYears.setEnabled(true);
        }
    }//GEN-LAST:event_chkNoOfYearsActionPerformed

    private void chkRenewUpToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRenewUpToActionPerformed
        // TODO add your handling code here:
        if (chkRenewUpTo.isSelected()) {
            chkNoOfYears.setSelected(false);
            txtNoOfYears.setEnabled(false);
            tdtRenewFrom.setEnabled(true);
            txtRenewalYears.setEnabled(true);
        } else {
            chkNoOfYears.setSelected(true);
            txtNoOfYears.setEnabled(true);
            txtRenewalYears.setEnabled(false);
        }
    }//GEN-LAST:event_chkRenewUpToActionPerformed

    public void calTotalRenewedAmount() {
        if (tblRiskFundData.getRowCount() > 0) {
            int count = tblRiskFundData.getRowCount();
            double selectedCount = 0.0;
            double riskFund = 0.0;
            double totalTax = 0.0;
            for (int i = 0; i < count; i++) {
                if ((Boolean) tblRiskFundData.getValueAt(i, 0)) {
                    selectedCount++;
                    riskFund += CommonUtil.convertObjToDouble(tblRiskFundData.getValueAt(i, 6));
                    totalTax += CommonUtil.convertObjToDouble(tblRiskFundData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblRiskFundData.getValueAt(i, 8)) + CommonUtil.convertObjToDouble(tblRiskFundData.getValueAt(i, 9));
                }
            }
            lblRiskFundSelectedRecords.setText(CommonUtil.convertObjToStr(selectedCount));
            lblTotalRiskFundVal.setText(CommonUtil.convertObjToStr(riskFund));
            lblTotalTaxVal.setText(CommonUtil.convertObjToStr(totalTax));
        }
    }

    private void chkRiskFundSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRiskFundSelectAllActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(tblRiskFundData, new Boolean(chkRiskFundSelectAll.isSelected()));
        
        for (int i = 0; i < tblRiskFundData.getRowCount(); i++) {           
            if (riskFundLimitList.contains(String.valueOf(i))) {
                tblRiskFundData.setValueAt(false, i, 0);
            }
        }
        calTotalRenewedAmount();
        
    }//GEN-LAST:event_chkRiskFundSelectAllActionPerformed

    private void tblRiskFundDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRiskFundDataMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRiskFundDataMousePressed

    private void tblRiskFundDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRiskFundDataMouseReleased
        // TODO add your handling code here:
        calTotalRenewedAmount();
    }//GEN-LAST:event_tblRiskFundDataMouseReleased

    private void tblRiskFundDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRiskFundDataMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblRiskFundDataMouseMoved

    private void btnRiskFundProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRiskFundProcessActionPerformed
        // TODO add your handling code here:      
        observable.setScreen(this.getScreen());
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                try {
                    ArrayList lstTotProcess = new ArrayList();

                    if (tblRiskFundData.getRowCount() > 0) {
                        for (int i = 0; i < tblRiskFundData.getRowCount(); i++) {
                            HashMap ProcessAcctMap = new HashMap();
                            if (((Boolean) tblRiskFundData.getValueAt(i, 0)).booleanValue()) {
                                for (int j = 1; j < tblRiskFundData.getColumnCount(); j++) {
                                    ProcessAcctMap.put("ACCT_NUM", tblRiskFundData.getValueAt(i, 1));
                                    ProcessAcctMap.put("PROD_ID", observable.getRiskFundProdId());
                                    ProcessAcctMap.put("PROD_DESC", observable.getRiskFundProdDesc());
                                    ProcessAcctMap.put("RISK_FUND", tblRiskFundData.getValueAt(i, 6));
                                    ProcessAcctMap.put("CGST", tblRiskFundData.getValueAt(i, 7));
                                    ProcessAcctMap.put("SGST", tblRiskFundData.getValueAt(i, 8));
                                    ProcessAcctMap.put("FLOOD_CESS", tblRiskFundData.getValueAt(i, 9));
                                }
                                lstTotProcess.add(ProcessAcctMap);
                            }
                        }
                    }

                    HashMap arbMap = new HashMap();
                    arbMap.put("KCC_RISK_FUND_LIST", lstTotProcess);
                    observable.processRiskFund(arbMap);
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

        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            System.out.println("return map :: "+ observable.getProxyReturnMap());
            if (observable.getProxyReturnMap() != null) {
                ClientUtil.showMessageWindow("Process Completed");
            } 
            observable.removeRowsFromRiskFundTable(tblRiskFundData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
            btnRiskFundClearActionPerformed(null);
        }else{
            observable.removeRowsFromRiskFundTable(tblRiskFundData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
            btnRiskFundClearActionPerformed(null);
        }


    }//GEN-LAST:event_btnRiskFundProcessActionPerformed

    private void btnRiskFundCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRiskFundCloseActionPerformed
        // TODO add your handling code here:
        observable.removeRowsFromRiskFundTable(tblRiskFundData);
        dispose();
    }//GEN-LAST:event_btnRiskFundCloseActionPerformed

    private void btnRiskFundClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRiskFundClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(panRiskFund);
        lblRiskFundNoOfRecordsVal.setText("");
        lblRiskFundSelectedRecords.setText("");
        lblTotalRiskFundVal.setText("");
        lblTotalTaxVal.setText("");
        lblProdDescription.setText("");
        observable.setRiskFundProdId("");
        observable.setRiskFundProdDesc("");
        observable.setRiskFundAcctHead("");
    }//GEN-LAST:event_btnRiskFundClearActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        if (tblData.getRowCount() > 0) {
            if (suretyColourList.contains(String.valueOf(tblData.getSelectedRow()))) {
                //tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
            }
            if (deathMarkedCustomerList.contains(String.valueOf(tblData.getSelectedRow()))) {
                tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
            }
            if (chkAllAccounts.isSelected()) {
                if (dueColourList.contains(String.valueOf(tblData.getSelectedRow()))) {
                    //tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
                }
            }
        }
        calTotalAmount();
    }//GEN-LAST:event_tblDataMouseClicked

    private void chkUnselectSuretyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUnselectSuretyActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tblData.getRowCount(); i++) {
            if (suretyColourList.contains(String.valueOf(i))) {
                if (chkUnselectSurety.isSelected()) {
                    tblData.setValueAt(false, i, 0);
                } else {
                    tblData.setValueAt(true, i, 0);
                }
            }
        }
        calTotalAmount();
    }//GEN-LAST:event_chkUnselectSuretyActionPerformed

    private void btnRenewalIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewalIdSearchActionPerformed
        // TODO add your handling code here:
        viewType = "RENEWAL_ID";
        popUp("RENEWAL_ID");
    }//GEN-LAST:event_btnRenewalIdSearchActionPerformed

    private void btnShowRenewedAcctsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowRenewedAcctsActionPerformed
        // TODO add your handling code here:
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_ID", observable.getRiskFundProdDesc());
        List chargeList = ClientUtil.executeQuery("getSelectRiskFundDetailsForKCC", whereMap);
        populateRiskFundData(chargeList);
    }//GEN-LAST:event_btnShowRenewedAcctsActionPerformed

    private void tblRiskFundDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRiskFundDataMouseClicked
        // TODO add your handling code here:
          if (tblRiskFundData.getRowCount() > 0) {
            if (riskFundLimitList.contains(String.valueOf(tblRiskFundData.getSelectedRow()))) {
                tblRiskFundData.setValueAt(new Boolean(false), tblRiskFundData.getSelectedRow(), 0);
            }            
        }
          calTotalRenewedAmount();
    }//GEN-LAST:event_tblRiskFundDataMouseClicked

    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    public void calTotalAmount() {
        if (tblData.getRowCount() > 0) {
            int count = tblData.getRowCount();
            double selectedCount = 0.0;
            for (int i = 0; i < count; i++) {
                if ((Boolean) tblData.getValueAt(i, 0)) {
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
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnRenewalIdSearch;
    private com.see.truetransact.uicomponent.CButton btnRiskFundClear;
    private com.see.truetransact.uicomponent.CButton btnRiskFundClose;
    private com.see.truetransact.uicomponent.CButton btnRiskFundProcess;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShowRenewedAccts;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel8;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkAllAccounts;
    private com.see.truetransact.uicomponent.CCheckBox chkDueOn;
    private com.see.truetransact.uicomponent.CCheckBox chkNoOfYears;
    private com.see.truetransact.uicomponent.CCheckBox chkRenewUpTo;
    private com.see.truetransact.uicomponent.CCheckBox chkRiskFundSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkUnselectSurety;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProdDescription;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProdType1;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundAccts;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblToDate1;
    private com.see.truetransact.uicomponent.CLabel lblTotalRiskFundVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalTaxVal;
    private com.see.truetransact.uicomponent.CPanel panArbit;
    private com.see.truetransact.uicomponent.CPanel panArbit1;
    private com.see.truetransact.uicomponent.CPanel panAward;
    private com.see.truetransact.uicomponent.CPanel panKCCREnewal;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch1;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch2;
    private com.see.truetransact.uicomponent.CPanel panRiskFund;
    private com.see.truetransact.uicomponent.CPanel panRiskFundTable;
    private com.see.truetransact.uicomponent.CPanel panRiskFundTotDetails;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch1;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcRiskFundTable;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblRiskFundData;
    private com.see.truetransact.uicomponent.CDateField tdtDueOnDt;
    private com.see.truetransact.uicomponent.CDateField tdtRenewFrom;
    private com.see.truetransact.uicomponent.CTextField txtNoOfYears;
    private com.see.truetransact.uicomponent.CTextField txtRenewalId;
    private com.see.truetransact.uicomponent.CTextField txtRenewalYears;
    // End of variables declaration//GEN-END:variables
}

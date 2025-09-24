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
package com.see.truetransact.ui.correctiontool;

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
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.TrueTransactMain;
import java.awt.*;
import java.util.LinkedHashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author balachandar
 */
public class DataCorrectionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private DataCorrectionOB observable;
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
    private final static Logger log = Logger.getLogger(DataCorrectionUI.class);
    boolean isEdit = false;
    ArrayList dueColourList = new ArrayList();
    ArrayList suretyColourList = new ArrayList();
    ArrayList riskFundLimitList =  new ArrayList();
    ArrayList deathMarkedCustomerList = new ArrayList();

    /**
     * Creates new form AuthorizeUI
     */
    public DataCorrectionUI() {
        setupInit();
        setupScreen();
        //tblData.setAutoCreateRowSorter(true);
        ClientUtil.enableDisable(panAward, true);
        panAward.setVisible(true);
        CboCorrectionType.setModel(observable.getCbmCorrectionType());
        cboNewProductName.setModel(observable.getCbmNewProductName());
        cboProductName.setModel(observable.getCbmProductName());
        cboTransType.setModel(observable.getCbmTransType());
        cboNewTransType2.setModel(observable.getCbmTransTypeCredit());
        cboNewTransType1.setModel(observable.getCbmTransTypeDebit());
        cboBranchCode.setModel(observable.getCbmbranch());
        cboIndendBranchCode.setModel(observable.getCbmIndendBranch());
        CboIndendCorrectionType.setModel(observable.getCbmIndendCorrectionType());
    }

    /**
     * Creates new form AuthorizeUI
     */
    public DataCorrectionUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        panAward.setVisible(false);
        ClientUtil.disableAll(panAward, false);
        CboCorrectionType.setModel(observable.getCbmProdId());
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
        ClientUtil.enableDisable(panIndendCorrections, false);
        cTabbedPane1.remove(panIndendCorrections);
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
            observable = new DataCorrectionOB();
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
        lblSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panAward = new com.see.truetransact.uicomponent.CPanel();
        rdoCash = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransfer = new com.see.truetransact.uicomponent.CRadioButton();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        lblTransId = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        txtTransId = new com.see.truetransact.uicomponent.CTextField();
        lblProductName = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNo = new com.see.truetransact.uicomponent.CLabel();
        cboProductName = new com.see.truetransact.uicomponent.CComboBox();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnAcctNo = new com.see.truetransact.uicomponent.CButton();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        btnSearchClear = new com.see.truetransact.uicomponent.CButton();
        btnTransId = new com.see.truetransact.uicomponent.CButton();
        panIssueType = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        cboBranchCode = new com.see.truetransact.uicomponent.CComboBox();
        lblCorrectionType = new com.see.truetransact.uicomponent.CLabel();
        CboCorrectionType = new com.see.truetransact.uicomponent.CComboBox();
        panAccountNoChange = new com.see.truetransact.uicomponent.CPanel();
        cboNewProductName = new com.see.truetransact.uicomponent.CComboBox();
        cboNewProdId = new com.see.truetransact.uicomponent.CComboBox();
        txtNewAcctNo = new com.see.truetransact.uicomponent.CTextField();
        lblNewPrductName = new com.see.truetransact.uicomponent.CLabel();
        lblNewProdId = new com.see.truetransact.uicomponent.CLabel();
        lblNewAcctNo = new com.see.truetransact.uicomponent.CLabel();
        btnNewAcctNo = new com.see.truetransact.uicomponent.CButton();
        panGoldStockChange = new com.see.truetransact.uicomponent.CPanel();
        txtNetWeight = new com.see.truetransact.uicomponent.CTextField();
        lblGoldItems = new com.see.truetransact.uicomponent.CLabel();
        cLabel13 = new com.see.truetransact.uicomponent.CLabel();
        cLabel14 = new com.see.truetransact.uicomponent.CLabel();
        txtGrossWeight = new com.see.truetransact.uicomponent.CTextField();
        txtGoldItems = new com.see.truetransact.uicomponent.CTextField();
        panOtherChange = new com.see.truetransact.uicomponent.CPanel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnAcHd = new com.see.truetransact.uicomponent.CButton();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txaNarration = new com.see.truetransact.uicomponent.CTextArea();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        lblHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        panTranTypeChange = new com.see.truetransact.uicomponent.CPanel();
        lblTransId1 = new com.see.truetransact.uicomponent.CLabel();
        lblTransId2 = new com.see.truetransact.uicomponent.CLabel();
        cboNewTransType1 = new com.see.truetransact.uicomponent.CComboBox();
        cboNewTransType2 = new com.see.truetransact.uicomponent.CComboBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnFormClear = new com.see.truetransact.uicomponent.CButton();
        panIndendCorrections = new com.see.truetransact.uicomponent.CPanel();
        panIndendIssueType = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCode1 = new com.see.truetransact.uicomponent.CLabel();
        cboIndendBranchCode = new com.see.truetransact.uicomponent.CComboBox();
        lblCorrectionType1 = new com.see.truetransact.uicomponent.CLabel();
        CboIndendCorrectionType = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        tdtIndendTransDt = new com.see.truetransact.uicomponent.CDateField();
        txtIRId = new com.see.truetransact.uicomponent.CTextField();
        btnIRIdSearch = new com.see.truetransact.uicomponent.CButton();
        btnIndendDisplay = new com.see.truetransact.uicomponent.CButton();
        btnIndendSearchClear = new com.see.truetransact.uicomponent.CButton();
        panIndendTable = new com.see.truetransact.uicomponent.CPanel();
        srcIndendTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblIndend = new com.see.truetransact.uicomponent.CTable();
        panGoldStockChange2 = new com.see.truetransact.uicomponent.CPanel();
        lblGoldItems2 = new com.see.truetransact.uicomponent.CLabel();
        cLabel17 = new com.see.truetransact.uicomponent.CLabel();
        cLabel18 = new com.see.truetransact.uicomponent.CLabel();
        txtLiabilityAmt = new com.see.truetransact.uicomponent.CTextField();
        newTransDt = new com.see.truetransact.uicomponent.CDateField();
        btnIndendSave = new com.see.truetransact.uicomponent.CButton();
        btnIndendClear = new com.see.truetransact.uicomponent.CButton();
        txtNewDepo = new com.see.truetransact.uicomponent.CTextField();
        btnDepoSearch = new com.see.truetransact.uicomponent.CButton();
        lblNewDepo = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Data Correction Tool");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));

        panKCCREnewal.setMinimumSize(new java.awt.Dimension(800, 35));
        panKCCREnewal.setPreferredSize(new java.awt.Dimension(800, 35));

        panSearchCondition.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 150));

        panAward.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panAward.setMaximumSize(new java.awt.Dimension(250, 150));
        panAward.setMinimumSize(new java.awt.Dimension(250, 150));
        panAward.setPreferredSize(new java.awt.Dimension(250, 150));

        rdoCash.setText("Cash");
        rdoCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCashActionPerformed(evt);
            }
        });

        rdoTransfer.setText("Transfer");
        rdoTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransferActionPerformed(evt);
            }
        });

        lblDate.setText("Date");

        lblTransType.setText("Trans Type");

        lblTransId.setText("Trans Id");

        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });

        cboTransType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTransTypeItemStateChanged(evt);
            }
        });

        lblProductName.setText("Product Name");

        lblProdType.setText("Product Id");

        lblAcctNo.setText("A/c No");

        cboProductName.setPopupWidth(200);
        cboProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductNameActionPerformed(evt);
            }
        });

        cboProdId.setPopupWidth(200);

        txtAcctNo.setAllowAll(true);

        btnAcctNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctNoActionPerformed(evt);
            }
        });

        btnDisplay.setText("Display");
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });

        btnSearchClear.setText("Clear");
        btnSearchClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchClearActionPerformed(evt);
            }
        });

        btnTransId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransIdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panAwardLayout = new javax.swing.GroupLayout(panAward);
        panAward.setLayout(panAwardLayout);
        panAwardLayout.setHorizontalGroup(
            panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAwardLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAwardLayout.createSequentialGroup()
                        .addComponent(lblTransId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panAwardLayout.createSequentialGroup()
                                .addComponent(btnDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSearchClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panAwardLayout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(btnTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtAcctNo, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panAwardLayout.createSequentialGroup()
                        .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panAwardLayout.createSequentialGroup()
                                .addComponent(rdoCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(rdoTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panAwardLayout.createSequentialGroup()
                                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panAwardLayout.createSequentialGroup()
                                        .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tdtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panAwardLayout.createSequentialGroup()
                                        .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cboTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panAwardLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panAwardLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(lblProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboProdId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panAwardLayout.setVerticalGroup(
            panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAwardLayout.createSequentialGroup()
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdoCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tdtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProdType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProdId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnTransId, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTransId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTransId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAwardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearchClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panIssueType.setBorder(javax.swing.BorderFactory.createTitledBorder("Issue Type Details"));
        panIssueType.setMinimumSize(new java.awt.Dimension(400, 65));
        panIssueType.setPreferredSize(new java.awt.Dimension(400, 65));

        lblBranchCode.setText("Branch Code");

        lblCorrectionType.setText("Correction Type");

        CboCorrectionType.setMinimumSize(new java.awt.Dimension(100, 21));
        CboCorrectionType.setPopupWidth(320);
        CboCorrectionType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CboCorrectionTypeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panIssueTypeLayout = new javax.swing.GroupLayout(panIssueType);
        panIssueType.setLayout(panIssueTypeLayout);
        panIssueTypeLayout.setHorizontalGroup(
            panIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIssueTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCorrectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CboCorrectionType, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panIssueTypeLayout.setVerticalGroup(
            panIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIssueTypeLayout.createSequentialGroup()
                .addGroup(panIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCorrectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CboCorrectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        panAccountNoChange.setBorder(javax.swing.BorderFactory.createTitledBorder("A/c Number change"));
        panAccountNoChange.setMaximumSize(new java.awt.Dimension(250, 150));
        panAccountNoChange.setMinimumSize(new java.awt.Dimension(250, 150));

        cboNewProductName.setPopupWidth(200);
        cboNewProductName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNewProductNameItemStateChanged(evt);
            }
        });
        cboNewProductName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNewProductNameActionPerformed(evt);
            }
        });

        cboNewProdId.setPopupWidth(200);

        txtNewAcctNo.setAllowAll(true);

        lblNewPrductName.setText("Product Name");

        lblNewProdId.setText("Product Id");

        lblNewAcctNo.setText("New A/c No");

        btnNewAcctNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNewAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewAcctNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panAccountNoChangeLayout = new javax.swing.GroupLayout(panAccountNoChange);
        panAccountNoChange.setLayout(panAccountNoChangeLayout);
        panAccountNoChangeLayout.setHorizontalGroup(
            panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAccountNoChangeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAccountNoChangeLayout.createSequentialGroup()
                        .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNewProdId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNewAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panAccountNoChangeLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtNewAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNewAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panAccountNoChangeLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cboNewProdId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38))))
                    .addGroup(panAccountNoChangeLayout.createSequentialGroup()
                        .addComponent(lblNewPrductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cboNewProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panAccountNoChangeLayout.setVerticalGroup(
            panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panAccountNoChangeLayout.createSequentialGroup()
                .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNewPrductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNewProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNewProdId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNewProdId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panAccountNoChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNewAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNewAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnNewAcctNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panGoldStockChange.setBorder(javax.swing.BorderFactory.createTitledBorder("Gold Stock Details"));
        panGoldStockChange.setMaximumSize(new java.awt.Dimension(250, 150));
        panGoldStockChange.setMinimumSize(new java.awt.Dimension(250, 150));

        txtNetWeight.setAllowAll(true);
        txtNetWeight.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetWeightFocusLost(evt);
            }
        });

        lblGoldItems.setText("Gold Items");

        cLabel13.setText("Gross Weight");

        cLabel14.setText("Net Weight");

        txtGrossWeight.setAllowAll(true);

        txtGoldItems.setAllowAll(true);

        javax.swing.GroupLayout panGoldStockChangeLayout = new javax.swing.GroupLayout(panGoldStockChange);
        panGoldStockChange.setLayout(panGoldStockChangeLayout);
        panGoldStockChangeLayout.setHorizontalGroup(
            panGoldStockChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGoldStockChangeLayout.createSequentialGroup()
                .addComponent(cLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGrossWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panGoldStockChangeLayout.createSequentialGroup()
                .addComponent(lblGoldItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtGoldItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panGoldStockChangeLayout.createSequentialGroup()
                .addComponent(cLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtNetWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panGoldStockChangeLayout.setVerticalGroup(
            panGoldStockChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGoldStockChangeLayout.createSequentialGroup()
                .addGroup(panGoldStockChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGoldItems, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGoldItems, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panGoldStockChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGrossWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panGoldStockChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNetWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        panOtherChange.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Changes"));
        panOtherChange.setMinimumSize(new java.awt.Dimension(423, 188));

        cLabel2.setText("Account Head");

        txtAcHd.setAllowAll(true);

        btnAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcHdActionPerformed(evt);
            }
        });

        cLabel3.setText("Particulars/Narration");

        txaNarration.setColumns(20);
        txaNarration.setRows(5);
        jScrollPane1.setViewportView(txaNarration);

        cLabel4.setText("Amount");

        txtAmt.setAllowAll(true);
        txtAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtFocusLost(evt);
            }
        });

        lblHeadDesc.setText("<<Head Description>>");

        javax.swing.GroupLayout panOtherChangeLayout = new javax.swing.GroupLayout(panOtherChange);
        panOtherChange.setLayout(panOtherChangeLayout);
        panOtherChangeLayout.setHorizontalGroup(
            panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOtherChangeLayout.createSequentialGroup()
                .addGroup(panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panOtherChangeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAcHd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAcHd, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panOtherChangeLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHeadDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panOtherChangeLayout.createSequentialGroup()
                                .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panOtherChangeLayout.setVerticalGroup(
            panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOtherChangeLayout.createSequentialGroup()
                .addGroup(panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAcHd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAcHd, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addComponent(lblHeadDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panOtherChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );

        panTranTypeChange.setBorder(javax.swing.BorderFactory.createTitledBorder("TransType Interchange"));

        lblTransId1.setText("<<TransId>>");

        lblTransId2.setText("<<TransId>>");

        cboNewTransType1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNewTransType1ItemStateChanged(evt);
            }
        });

        cboNewTransType2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboNewTransType2ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panTranTypeChangeLayout = new javax.swing.GroupLayout(panTranTypeChange);
        panTranTypeChange.setLayout(panTranTypeChangeLayout);
        panTranTypeChangeLayout.setHorizontalGroup(
            panTranTypeChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTranTypeChangeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panTranTypeChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTransId2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTransId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panTranTypeChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboNewTransType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNewTransType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        panTranTypeChangeLayout.setVerticalGroup(
            panTranTypeChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTranTypeChangeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panTranTypeChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNewTransType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panTranTypeChangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTransId2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNewTransType2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panSearchConditionLayout = new javax.swing.GroupLayout(panSearchCondition);
        panSearchCondition.setLayout(panSearchConditionLayout);
        panSearchConditionLayout.setHorizontalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addComponent(panIssueType, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panAward, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panAccountNoChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panTranTypeChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panGoldStockChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panOtherChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(4, Short.MAX_VALUE))
        );
        panSearchConditionLayout.setVerticalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addComponent(panIssueType, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panAccountNoChange, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panGoldStockChange, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(panTranTypeChange, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panSearchConditionLayout.createSequentialGroup()
                        .addComponent(panAward, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panOtherChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panTable.setPreferredSize(new java.awt.Dimension(600, 350));
        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setViewport(srcTable.getRowHeader());

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

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

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));

        javax.swing.GroupLayout cPanel2Layout = new javax.swing.GroupLayout(cPanel2);
        cPanel2.setLayout(cPanel2Layout);
        cPanel2Layout.setHorizontalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 384, Short.MAX_VALUE)
        );
        cPanel2Layout.setVerticalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 77, Short.MAX_VALUE)
        );

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnFormClear.setText("Clear");
        btnFormClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFormClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panKCCREnewalLayout = new javax.swing.GroupLayout(panKCCREnewal);
        panKCCREnewal.setLayout(panKCCREnewalLayout);
        panKCCREnewalLayout.setHorizontalGroup(
            panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panKCCREnewalLayout.createSequentialGroup()
                .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panKCCREnewalLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panKCCREnewalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panKCCREnewalLayout.createSequentialGroup()
                        .addGap(350, 350, 350)
                        .addComponent(cPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panKCCREnewalLayout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnFormClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panKCCREnewalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 759, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(31, Short.MAX_VALUE)))
        );
        panKCCREnewalLayout.setVerticalGroup(
            panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panKCCREnewalLayout.createSequentialGroup()
                .addContainerGap(286, Short.MAX_VALUE)
                .addComponent(cPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFormClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panKCCREnewalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panSearchCondition, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(62, 62, 62)))
        );

        cTabbedPane1.addTab("Data Correction", panKCCREnewal);

        panIndendIssueType.setBorder(javax.swing.BorderFactory.createTitledBorder("Issue Type Details"));
        panIndendIssueType.setMinimumSize(new java.awt.Dimension(400, 65));

        lblBranchCode1.setText("Branch Code");

        lblCorrectionType1.setText("Correction Type");

        CboIndendCorrectionType.setMinimumSize(new java.awt.Dimension(100, 21));
        CboIndendCorrectionType.setPopupWidth(320);
        CboIndendCorrectionType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CboIndendCorrectionTypeItemStateChanged(evt);
            }
        });

        cLabel1.setText("Trans Date");

        cLabel5.setText("IR Id");

        btnIRIdSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIRIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIRIdSearchActionPerformed(evt);
            }
        });

        btnIndendDisplay.setText("Display");
        btnIndendDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIndendDisplayActionPerformed(evt);
            }
        });

        btnIndendSearchClear.setText("Clear");
        btnIndendSearchClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIndendSearchClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panIndendIssueTypeLayout = new javax.swing.GroupLayout(panIndendIssueType);
        panIndendIssueType.setLayout(panIndendIssueTypeLayout);
        panIndendIssueTypeLayout.setHorizontalGroup(
            panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIndendIssueTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBranchCode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCorrectionType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboIndendBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CboIndendCorrectionType, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tdtIndendTransDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panIndendIssueTypeLayout.createSequentialGroup()
                        .addComponent(txtIRId, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnIRIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panIndendIssueTypeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnIndendDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnIndendSearchClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(259, 259, 259))
        );
        panIndendIssueTypeLayout.setVerticalGroup(
            panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIndendIssueTypeLayout.createSequentialGroup()
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBranchCode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboIndendBranchCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tdtIndendTransDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCorrectionType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CboIndendCorrectionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnIRIdSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIRId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(panIndendIssueTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnIndendDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIndendSearchClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panIndendTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panIndendTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panIndendTable.setPreferredSize(new java.awt.Dimension(600, 350));
        panIndendTable.setLayout(new java.awt.GridBagLayout());

        srcIndendTable.setViewport(srcTable.getRowHeader());

        tblIndend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblIndend.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblIndend.setMinimumSize(new java.awt.Dimension(350, 80));
        tblIndend.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblIndend.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblIndendMouseMoved(evt);
            }
        });
        tblIndend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblIndendMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblIndendMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblIndendMouseReleased(evt);
            }
        });
        srcIndendTable.setViewportView(tblIndend);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panIndendTable.add(srcIndendTable, gridBagConstraints);

        panGoldStockChange2.setBorder(javax.swing.BorderFactory.createTitledBorder("New Values"));
        panGoldStockChange2.setMaximumSize(new java.awt.Dimension(250, 150));
        panGoldStockChange2.setMinimumSize(new java.awt.Dimension(250, 150));

        lblGoldItems2.setText("Liability Amount");

        cLabel17.setText("New Depo");

        cLabel18.setText("New Transaction Date");

        txtLiabilityAmt.setAllowAll(true);

        btnIndendSave.setText("Save");
        btnIndendSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIndendSaveActionPerformed(evt);
            }
        });

        btnIndendClear.setText("Clear");
        btnIndendClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIndendClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panGoldStockChange2Layout = new javax.swing.GroupLayout(panGoldStockChange2);
        panGoldStockChange2.setLayout(panGoldStockChange2Layout);
        panGoldStockChange2Layout.setHorizontalGroup(
            panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGoldStockChange2Layout.createSequentialGroup()
                .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGoldItems2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLiabilityAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newTransDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panGoldStockChange2Layout.createSequentialGroup()
                        .addComponent(txtNewDepo, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDepoSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNewDepo, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(80, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panGoldStockChange2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnIndendSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnIndendClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(285, 285, 285))
        );
        panGoldStockChange2Layout.setVerticalGroup(
            panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGoldStockChange2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGoldItems2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLiabilityAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panGoldStockChange2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNewDepo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDepoSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panGoldStockChange2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblNewDepo, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newTransDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(panGoldStockChange2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIndendSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIndendClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout panIndendCorrectionsLayout = new javax.swing.GroupLayout(panIndendCorrections);
        panIndendCorrections.setLayout(panIndendCorrectionsLayout);
        panIndendCorrectionsLayout.setHorizontalGroup(
            panIndendCorrectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIndendCorrectionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panIndendCorrectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panIndendIssueType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panGoldStockChange2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panIndendCorrectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panIndendCorrectionsLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(panIndendTable, javax.swing.GroupLayout.PREFERRED_SIZE, 755, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panIndendCorrectionsLayout.setVerticalGroup(
            panIndendCorrectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIndendCorrectionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panIndendIssueType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                .addComponent(panGoldStockChange2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addGroup(panIndendCorrectionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panIndendCorrectionsLayout.createSequentialGroup()
                    .addGap(138, 138, 138)
                    .addComponent(panIndendTable, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(251, Short.MAX_VALUE)))
        );

        cTabbedPane1.addTab("Indend Corrections", panIndendCorrections);

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

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*
                 * (evt.getClickCount() == 2) &&
                 */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
        }
    }//GEN-LAST:event_tblDataMouseReleased
    
    private void actOpeningDtValidation(){
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());  
        String actNo =  CommonUtil.convertObjToStr(txtNewAcctNo.getText());
        Date transDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
        //getAccountOpeningDate
        HashMap dataMap =  new HashMap();
        dataMap.put("ACT_NUM",actNo);
        dataMap.put("PROD_TYPE",prodType);
        List dataList = ClientUtil.executeQuery("getAccountOpeningDate", dataMap);
        if(dataList != null && dataList.size() > 0){
            dataMap = (HashMap)dataList.get(0);
            if(dataMap.containsKey("OPEN_DATE") && dataMap.get("OPEN_DATE") != null){
                Date openDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("OPEN_DATE")));
                if(DateUtil.dateDiff(openDt, transDt) < 0){
                    ClientUtil.showMessageWindow("Could not change to this account number - " + actNo +" Open Date : " + DateUtil.getStringDate(openDt));
                    txtNewAcctNo.setText("");
                }
            }
        }
        
    }
    
    
    public void fillData(Object param) {
        String prodType = "";
        String prodId = "";
        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (viewType.equalsIgnoreCase("ACCT_NO")) {
            prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductName.getModel()).getKeyForSelected());
            prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());

            if (prodType.equals("GL")) {
                txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            } else {
                txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            }
            clearTableData();
        } else if (viewType.equalsIgnoreCase("NEW_ACCT_NO")) {
            String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
            prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());

            if (prodType.equals("GL")) {
                txtNewAcctNo.setText(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            } else {
                txtNewAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                actOpeningDtValidation();
            }
            // Check for account opening date
            
        }else if (viewType.equalsIgnoreCase("TRANS_ID")) {
           String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
           if(correctionType.equals("TRANS_AMT_CHANGE") || correctionType.equals("TRANS_TYPE_INTERCHANGE")){
             txtTransId.setText(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));  
           }else{
             txtTransId.setText(CommonUtil.convertObjToStr(hash.get("TRANS_ID")));
           }  
           if(hash.containsKey("ACT_NUM") && hash.get("ACT_NUM") != null && CommonUtil.convertObjToStr(hash.get("ACT_NUM")).length() > 0){
               txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
               cboProductName.setSelectedItem((observable.getCbmProductName()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_TYPE"))));
               cboProdId.setSelectedItem((observable.getCbmProdId()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_ID"))));
                if(correctionType.equals("OA_ACTNO_CHANGE") || correctionType.equals("SUSPENSE_ACTNO_CHANGE")){
                 cboNewProductName.setSelectedItem((observable.getCbmProductName()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_TYPE"))));
                 cboNewProdId.setSelectedItem((observable.getCbmProdId()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_ID"))));
                 cboNewProductName.setEnabled(false);
                 cboNewProdId.setEnabled(false);
                 cboProductName.setEnabled(false);
                 cboProdId.setEnabled(false);
                 btnAcctNo.setEnabled(false);
                }else if(correctionType.equals("OA_TO_ALL_ACTNO_CHANGE")||correctionType.equals("SA_TO_ALL_ACTNO_CHANGE")||correctionType.equals("AB_TO_ALL_ACTNO_CHANGE")||correctionType.equals("GL_TO_ACTNO_MAPPING")){//Ver 2.0
                 cboNewProductName.setEnabled(true);
                 cboNewProdId.setEnabled(true);
                 cboProductName.setEnabled(false);
                 cboProdId.setEnabled(false);
                 btnAcctNo.setEnabled(false);
                }else if(correctionType.equals("OTHERBANK_ACTNO_CHANGE")){
                 cboNewProductName.setSelectedItem((observable.getCbmProductName()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_TYPE"))));
                 //cboNewProdId.setSelectedItem((observable.getCbmProdId()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_ID"))));
                 cboNewProductName.setEnabled(false);
                 cboProductName.setEnabled(false);
                 cboProdId.setEnabled(false); 
                 btnAcctNo.setEnabled(false);
                }
                clearTableData();
           }
            if (correctionType.equals("GL_TO_ACTNO_MAPPING")) {
                txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
                System.out.println("prod type :: " + (observable.getCbmProductName()).getDataForKey(CommonUtil.convertObjToStr("OA")));
                cboProductName.setSelectedItem((observable.getCbmProductName()).getDataForKey(CommonUtil.convertObjToStr(hash.get("PROD_TYPE"))));
               
                cboNewProductName.setEnabled(true);
                cboNewProdId.setEnabled(true);
                cboProductName.setEnabled(false);
                cboProdId.setEnabled(false);
                btnAcctNo.setEnabled(false);
                clearTableData();
            }
           cboTransType.setSelectedItem((observable.getCbmTransType()).getDataForKey(CommonUtil.convertObjToStr(hash.get("TRANS_TYPE"))));
        }else if(viewType.equalsIgnoreCase("AC_HD_ID")){
             final String ACCOUNTHEAD = (String) hash.get("ACCOUNT HEAD");
             final String ACCOUNTHEADDESC = (String) hash.get("ACCOUNT HEAD DESCRIPTION");
             txtAcHd.setText(ACCOUNTHEAD);
             lblHeadDesc.setText(ACCOUNTHEADDESC);
        }else if(viewType.equalsIgnoreCase("IR_ID")){
             txtIRId.setText(CommonUtil.convertObjToStr(hash.get("IRID")));
        }
    }

    private void popUp(String field) {
        viewType = field;
        final HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String transIdMapName = "";
        String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            
        if (viewType.equals("ACCT_NO")) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductName.getModel()).getKeyForSelected());
            String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            whereMap.put("PROD_ID", prodId);
            whereMap.put("SELECTED_BRANCH", branchCode);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (prodType.equals("TD") || prodType.equals("TL") || prodType.equals("AB")) {
                if (prodType.equals("TL") || prodType.equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"  + prodType);
            } else if (prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + prodType);
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + prodType);
            }
        }else if (viewType.equals("NEW_ACCT_NO")) {
            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
            String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());
            whereMap.put("PROD_ID", prodId);
            //whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            if (prodType.equals("TD") || prodType.equals("TL") || prodType.equals("AB")) {
                if (prodType.equals("TL") || prodType.equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"  + prodType);
            } else if (prodType.equals("GL")) {
                viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + prodType);
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + prodType);
            }
        }
//        else if (viewType.equals("NEW_ACCT_NO")) {
//            String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
//            String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());
//            whereMap.put("PROD_ID", prodId);
//            whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
//            if (prodType.equals("TD") || prodType.equals("TL") || prodType.equals("AB")) {
//                if (prodType.equals("TL") || prodType.equals("AB")) {
//                    whereMap.put("RECEIPT", "RECEIPT");
//                }
//                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"  + prodType);
//            } else if (prodType.equals("GL")) {
//                viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + prodType);
//            } else {
//                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + prodType);
//            }
//        }
        else if (viewType.equals("TRANS_ID")) {
            String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            String transType = "";
            if(cboTransType.getSelectedIndex() > 0){
                transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
            }
            whereMap.put("CORRECTION_TYPE", correctionType);
            whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            whereMap.put(CommonConstants.BRANCH_ID, branchCode);
            if(correctionType.equals("OA_ACTNO_CHANGE")||correctionType.equals("OTHERBANK_ACTNO_CHANGE") && correctionType.equals("SUSPENSE_ACTNO_CHANGE")|| correctionType.equals("OA_TO_ALL_ACTNO_CHANGE")
                    ||correctionType.equals("SA_TO_ALL_ACTNO_CHANGE")||correctionType.equals("AB_TO_ALL_ACTNO_CHANGE") || correctionType.equals("GL_TO_ACTNO_MAPPING")){
               whereMap.put("ACT_NUM_CHANGE", correctionType); 
            }           
            
            if(correctionType.equals("TRANS_AMT_CHANGE") || correctionType.equals("TRANS_TYPE_INTERCHANGE")){
                transIdMapName = "getSelectTransferTransIdForDataCorrection";
            }else{
                if (rdoCash.isSelected()) {
                    if(transType.equals("DEBIT")){
                       whereMap.put("TRANS_TYPE","DEBIT"); 
                    }else if(transType.equals("CREDIT")){
                       whereMap.put("TRANS_TYPE","CREDIT");  
                    }
                    transIdMapName = "getSelectCashTransIdForDataCorrection";
                } else if (rdoTransfer.isSelected()) {
                    if(transType.equals("DEBIT")){
                       whereMap.put("TRANS_TYPE","DEBIT"); 
                    }else if(transType.equals("CREDIT")){
                       whereMap.put("TRANS_TYPE","CREDIT");  
                    }
                    transIdMapName = "getSelectTransferTransIdForDataCorrection";
                }
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, transIdMapName);
        } else if (viewType.equals("AC_HD_ID")) {            
            whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);           
            viewMap.put(CommonConstants.MAP_NAME, "data.correction.getAcctHeadList");
        }else if (viewType.equals("IR_ID")) {
            String indendBranchcode = CommonUtil.convertObjToStr(((ComboBoxModel) cboIndendBranchCode.getModel()).getKeyForSelected());
            String indendCorrectionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboIndendCorrectionType.getModel()).getKeyForSelected());
            Date indendTransDt =  DateUtil.getDateMMDDYYYY(tdtIndendTransDt.getDateValue());
            whereMap.put("BRANCH_CODE",indendBranchcode);
            whereMap.put("CORRECTION_TYPE",indendCorrectionType);
            whereMap.put("TRANS_DT", indendTransDt);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);           
            viewMap.put(CommonConstants.MAP_NAME, "getSelectIRIDForDataCorrection");
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
        String mapName = "";
        if(rdoCash.isSelected()){
            mapName = "getSelectAllCashDataForCorrection";
        }else if(rdoTransfer.isSelected()){
            mapName = "getSelectAllTransferDataForCorrection";
        }
        System.out.println("chkng");
        if (isOK) {
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, mapName);          
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            whereMap.put("BRANCH_CODE", branchCode);
            if(txtTransId.getText().length() > 0){
              whereMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            }
            
            if (!correctionType.equals("NARRATION_CHANGE") && !correctionType.equals("HEAD_CHANGE") && !correctionType.equals("GL_TO_ACTNO_MAPPING") && !correctionType.equals("TRANS_NARRATION_CHANGE")) {
                if (txtAcctNo.getText().length() > 0) {
                    whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcctNo.getText()));
                }
            }
            
//            if(txtAcctNo.getText().length() > 0){
//              whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcctNo.getText()));
//            }
            if(cboTransType.getSelectedIndex() > 0){
             String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
              whereMap.put("TRANS_TYPE", transType);
            }
            whereMap.put("CORRECTION_TYPE", correctionType);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateData(viewMap, tblData);               
                panAward.setVisible(true);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    
    public void populateTransAmtChangeData() {
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        String mapName = "";
        mapName = "getSelectAllTransferAmtDataForCorrection";
        System.out.println("chkng");
        if (isOK) {
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, mapName);          
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            whereMap.put("BRANCH_CODE", branchCode);
            if(txtTransId.getText().length() > 0){
              whereMap.put("BATCH_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            }
            whereMap.put("CORRECTION_TYPE", correctionType);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateTransAmtChangeData(viewMap, tblData);               
                panAward.setVisible(true);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void populateTransIdData() {
       if(tblData.getRowCount() > 0){
           lblTransId1.setText(CommonUtil.convertObjToStr(tblData.getValueAt(0, 0)));
           lblTransId2.setText(CommonUtil.convertObjToStr(tblData.getValueAt(1, 0)));
       }
    }
    
    public void populateTransTypeInterChangeData() {
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        String mapName = "";
        mapName = "getSelectAllTransferAmtDataForCorrection";
        System.out.println("chkng");
        if (isOK) {
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, mapName);          
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            whereMap.put("BRANCH_CODE", branchCode);
            if(txtTransId.getText().length() > 0){
              whereMap.put("BATCH_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            }
            whereMap.put("CORRECTION_TYPE", correctionType);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateTransTypeInterChangeData(viewMap, tblData);               
                panAward.setVisible(true);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void populateSecurityData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String mapName = "getSelectSecurityDetailsForCorrection";
        String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
        String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
        viewMap.put(CommonConstants.MAP_NAME, mapName);
        whereMap.put("TODAY_DT", getProperDate(currDt));
        whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
        whereMap.put("BRANCH_CODE", branchCode);
        if (txtAcctNo.getText().length() > 0) {
            whereMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcctNo.getText()));
        }
        whereMap.put("CORRECTION_TYPE", correctionType);
        whereMap.put("CURR_DATE", getProperDate(currDt));
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateSecurityData(viewMap, tblData);
            if(correctionType.equals("GOLD_ITEM_CHANGE")){
                txtGoldItems.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(tblData.getValueAt(0, 10))));
            }
            panAward.setVisible(true);
            heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
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
            //observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }

   

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
        if (tblData.getRowCount() > 0) {
            if (suretyColourList.contains(String.valueOf(tblData.getSelectedRow()))) {
                //tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
            }
            if (deathMarkedCustomerList.contains(String.valueOf(tblData.getSelectedRow()))) {
                tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
            }
            
        }
        calTotalAmount();
    }//GEN-LAST:event_tblDataMouseClicked

    private void rdoTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransferActionPerformed
        // TODO add your handling code here:
        if(rdoTransfer.isSelected()){
            rdoCash.setSelected(false);
        }else{
            rdoCash.setSelected(true);
            rdoTransfer.setSelected(false);
        }
         clearTableData();
    }//GEN-LAST:event_rdoTransferActionPerformed

    private void cboNewProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNewProductNameActionPerformed
        // TODO add your handling code here:
        if (cboNewProductName.getSelectedIndex() > 0) {
            System.out.println("tblData.getRowCount() :: " + tblData.getRowCount());
           
                System.out.println("Execute hereeee");
                String prodType = ((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected().toString();
                System.out.println("prodType here:::: " + prodType);
                observable.setCbmNewProdId(prodType);
                cboNewProdId.setModel(observable.getCbmNewProdId());
            
        }
    }//GEN-LAST:event_cboNewProductNameActionPerformed

    private void cboProductNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductNameActionPerformed
        // TODO add your handling code here:
        if (cboProductName.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProductName.getModel()).getKeyForSelected().toString();
            String correctionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            observable.setCbmProdId(prodType,correctionType);
            cboProdId.setModel(observable.getCbmProdId());
        }
    }//GEN-LAST:event_cboProductNameActionPerformed

    private void btnAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctNoActionPerformed
        // TODO add your handling code here
        if(cboProductName.getSelectedIndex() == 0){
            ClientUtil.showMessageWindow("Select Product Name !!!");
        } else if(cboProdId.getSelectedIndex() == 0){
           ClientUtil.showMessageWindow("Select Product Id !!!"); 
        }else{
          popUp("ACCT_NO");
        }
    }//GEN-LAST:event_btnAcctNoActionPerformed

    private void btnNewAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewAcctNoActionPerformed
        // TODO add your handling code here:
        if(cboNewProductName.getSelectedIndex() == 0){
            ClientUtil.showMessageWindow("Select Product Name !!!");
        } else if(cboNewProdId.getSelectedIndex() == 0){
           ClientUtil.showMessageWindow("Select Product Id !!!"); 
        }else{
            popUp("NEW_ACCT_NO");
        }
    }//GEN-LAST:event_btnNewAcctNoActionPerformed

    private void btnTransIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransIdActionPerformed
        // TODO add your handling code here:
        if (CboCorrectionType.getSelectedIndex() == 0) {
            ClientUtil.showMessageWindow("Select Correction type !!!");
        } else if (cboBranchCode.getSelectedIndex() == 0) {
            ClientUtil.showMessageWindow("Select branch code !!!");
        } else if (!rdoCash.isSelected() && !rdoTransfer.isSelected()) {
            ClientUtil.showMessageWindow("Select Cash or Transfer !!!");
        } else if (tdtDate.getDateValue().length() == 0) {
            ClientUtil.showMessageWindow("Enter transaction Date !!!");
        } else if (!transDtAllowed()) {
            tdtDate.setDateValue(null);
        } else {
            popUp("TRANS_ID");
        }
    }//GEN-LAST:event_btnTransIdActionPerformed

    private boolean transDtAllowed(){
        boolean transAllowed =  true;
         Date maxBackTransDt = null;
            HashMap cbmsMap = new HashMap();
            List list;
            list = ClientUtil.executeQuery("getTillDateForDataCorrection", null);
            if (list != null && list.size() > 0) {
                cbmsMap = (HashMap) list.get(0);
                maxBackTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cbmsMap.get("CORRECTION_DATE")));
            }
            Date enteredDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
            if ((maxBackTransDt != null && (DateUtil.dateDiff(maxBackTransDt, enteredDt)) < 0)) {
                ClientUtil.showMessageWindow("Corrections allowed only\n From :" + DateUtil.getStringDate(maxBackTransDt) +"  To :" + DateUtil.getStringDate(currDt));
               transAllowed = false;
            }
        return transAllowed;
    }
    
    
    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        //GOLD_ITEM_CHANGE
        if(cboBranchCode.getSelectedIndex() == 0){
           ClientUtil.showMessageWindow("Select branch code !!!"); 
        }else if(CboCorrectionType.getSelectedIndex() > 0){
            clearTableData();
            String correctionType =  CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
            if (!correctionType.equals("GOLD_ITEM_CHANGE") && !correctionType.equals("GOLD_WEIGHT_CHANGE")) {                
                if (correctionType.equals("TRANS_AMT_CHANGE")) {
                    if (!rdoTransfer.isSelected()) {
                        ClientUtil.showMessageWindow("Select Transfer !!!");
                    } else if (tdtDate.getDateValue().length() == 0) {
                        ClientUtil.showMessageWindow("Enter transaction Date !!!");
                    } else if (txtTransId.getText().length() == 0) {
                        ClientUtil.showMessageWindow("Transaction ID should not be empty !!!");
                    } else if (!transDtAllowed()) {
                        tdtDate.setDateValue(null);
                    } else {
                        populateTransAmtChangeData();
                        tblData.getColumnModel().getColumn(1).setPreferredWidth(90);
                        tblData.getColumnModel().getColumn(7).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(8).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(4).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(6).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(7).setPreferredWidth(250);
                    }
                }else if (correctionType.equals("TRANS_TYPE_INTERCHANGE")) {
                    if (!rdoTransfer.isSelected()) {
                        ClientUtil.showMessageWindow("Select Transfer !!!");
                    } else if (tdtDate.getDateValue().length() == 0) {
                        ClientUtil.showMessageWindow("Enter transaction Date !!!");
                    } else if (txtTransId.getText().length() == 0) {
                        ClientUtil.showMessageWindow("Transaction ID should not be empty !!!");
                    } else if (!transDtAllowed()) {
                        tdtDate.setDateValue(null);
                    } else {
                        
                        populateTransTypeInterChangeData();
                        populateTransIdData();
                        tblData.getColumnModel().getColumn(1).setPreferredWidth(90);
                        tblData.getColumnModel().getColumn(7).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(8).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(4).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(6).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(7).setPreferredWidth(250);
                    }
                } else {
                    if (!rdoCash.isSelected() && !rdoTransfer.isSelected()) {
                        ClientUtil.showMessageWindow("Select Cash or Transfer !!!");
                    } else if (tdtDate.getDateValue().length() == 0) {
                        ClientUtil.showMessageWindow("Enter transaction Date !!!");
                    } else if (txtTransId.getText().length() == 0) {
                        ClientUtil.showMessageWindow("Transaction ID should not be empty !!!");
                    } else if (!transDtAllowed()) {
                        tdtDate.setDateValue(null);
                    } else {
                        populateData();
                        tblData.getColumnModel().getColumn(1).setPreferredWidth(90);
                        tblData.getColumnModel().getColumn(7).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(8).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(4).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(6).setPreferredWidth(100);
                        tblData.getColumnModel().getColumn(7).setPreferredWidth(250);
                        if(correctionType.equals("OA_ACTNO_CHANGE")||correctionType.equals("SUSPENSE_ACTNO_CHANGE")){
                            enableDisableAccountNumberChangePanel(true);
                            cboNewProductName.setEnabled(false);
                            cboNewProdId.setEnabled(false);
                            txtNewAcctNo.setEnabled(false);
                        }else if(correctionType.equals("OTHERBANK_ACTNO_CHANGE")){
                            enableDisableAccountNumberChangePanel(true);
                            cboNewProductName.setEnabled(false);
                            cboNewProdId.setEnabled(true);
                            txtNewAcctNo.setEnabled(false);
                        }else if(correctionType.equals("OA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("SA_TO_ALL_ACTNO_CHANGE") ||correctionType.equals("AB_TO_ALL_ACTNO_CHANGE")||correctionType.equals("GL_TO_ACTNO_MAPPING")){
                            enableDisableAccountNumberChangePanel(true);
                            txtNewAcctNo.setEnabled(false);
                        }
                    }
                }

            }else{
                 if (txtAcctNo.getText().length() == 0) {
                    ClientUtil.showMessageWindow("Account No should not be empty !!!");
                 }else{
                    populateSecurityData();
                 }
            }
        }else{
            ClientUtil.showMessageWindow("Select Correction type !!!"); 
        }
        
        /*if(cboBranchCode.getSelectedIndex() == 0){
           ClientUtil.showMessageWindow("Select branch code !!!"); 
        }else if(CboCorrectionType.getSelectedIndex() == 0){
           ClientUtil.showMessageWindow("Select Correction type !!!"); 
        }else if(!rdoCash.isSelected() && !rdoTransfer.isSelected()){
           ClientUtil.showMessageWindow("Select Cash or Transfer !!!"); 
        } else if(tdtDate.getDateValue().length() == 0){
            ClientUtil.showMessageWindow("Enter transaction Date !!!");
        } else if(txtTransId.getText().length() == 0){
            ClientUtil.showMessageWindow("Transaction ID should not be empty !!!");
        } else{
            populateData();
            tblData.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblData.getColumnModel().getColumn(7).setPreferredWidth(100);
            tblData.getColumnModel().getColumn(8).setPreferredWidth(100);
        }*/
    }//GEN-LAST:event_btnDisplayActionPerformed

    private void btnSearchClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchClearActionPerformed
        // TODO add your handling code here:
        tdtDate.setDateValue(null);
        if(cboProdId.getItemCount() > 0)
           cboProdId.setSelectedIndex(0);
        if(cboProductName.getItemCount() > 0)
           cboProductName.setSelectedIndex(0);
        cboTransType.setSelectedIndex(0);
        txtTransId.setText("");
        txtAcctNo.setText("");
        rdoCash.setSelected(false);
        rdoTransfer.setSelected(false);
        
    }//GEN-LAST:event_btnSearchClearActionPerformed

    
    private void clearAccountNumberChangePanel(){
        txtNewAcctNo.setText("");
        if(cboNewProductName.getItemCount() > 0)
             cboNewProductName.setSelectedIndex(0);
        if(cboNewProdId.getItemCount() > 0)
             cboNewProdId.setSelectedIndex(0);
        
    }
    
     private void enableDisableAccountNumberChangePanel(boolean flag){
        txtNewAcctNo.setEnabled(flag);
        cboNewProdId.setEnabled(flag);
        cboNewProductName.setEnabled(flag);
        btnNewAcctNo.setEnabled(flag);
    }
     
     
     private void enableDisableTextFields(boolean flag){
        txtNewAcctNo.setEnabled(flag);
        txtAcctNo.setEnabled(flag);
        txtTransId.setEnabled(flag);
        txtAcHd.setEnabled(flag);
    }
     
       
    private void clearGoldItemChangePanel(){
        txtGoldItems.setText("");
        txtGrossWeight.setText("");
        txtNetWeight.setText("");
    }
    
     private void enableDisableGoldItemChangePanel(boolean flag){
        txtGoldItems.setEnabled(flag);
        txtGrossWeight.setEnabled(flag);
        txtNetWeight.setEnabled(flag);
    }
     
    private void clearTransTypeInterChangePanel(){
        cboNewTransType1.setSelectedIndex(0);
        cboNewTransType2.setSelectedIndex(0);
    }
    
     private void enableDisableTransTypeInterChangePanel(boolean flag){
        cboNewTransType1.setEnabled(flag);
        cboNewTransType2.setEnabled(flag);
    }
     
    private void clearOtherChangePanel(){
        txtAcHd.setText("");
        txtAmt.setText("");
        txaNarration.setText("");
        lblHeadDesc.setText("<<Head Description>>");
    }
    
     private void enableDisableOtherChangePanel(boolean flag){
        txtAcHd.setEnabled(flag);
        txtAmt.setEnabled(flag);
        txaNarration.setEnabled(flag);
        btnAcHd.setEnabled(flag);
    } 
     
   private void clearIssueTypePanel(){
      cboBranchCode.setSelectedIndex(0);
      CboCorrectionType.setSelectedIndex(0);
    }
    
     private void enableDisableIssueTypePanel(boolean flag){
        cboBranchCode.setEnabled(flag);
        CboCorrectionType.setEnabled(flag);       
    }
     
     private void clearTableData() {        
        ClientUtil.clearAll(panTable);
    }    
    
    private void btnFormClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFormClearActionPerformed
        // TODO add your handling code here:
        btnSearchClearActionPerformed(null);
        clearAccountNumberChangePanel();
        clearGoldItemChangePanel();    
        clearTransTypeInterChangePanel();
        clearOtherChangePanel();
        clearIssueTypePanel();
        clearTableData();
    }//GEN-LAST:event_btnFormClearActionPerformed

    private void btnAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcHdActionPerformed
        // TODO add your handling code here:
        popUp("AC_HD_ID");
    }//GEN-LAST:event_btnAcHdActionPerformed

    private void btnHeadChangeSaveAction(String correctionType){  
        if(txtAcHd.getText().length() == 0){
            ClientUtil.showMessageWindow("New Account Head should not be empty !!!");
        }else{
            String newValue = "";
            String oldValue = "";
            String transMode = "";
            HashMap dataMap = new HashMap();
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            System.out.println("branch code :: " + branchCode);
            System.out.println("correctionType :: " + correctionType);
            String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
            String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
            String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
            String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
            String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
            String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));
            String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());

            DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
            if (objDataCorrectionApproveUI.isCancelActionKey()) {
                return;
            }
            String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
            String remarks = objDataCorrectionApproveUI.getTxtRemarks();
            newValue = CommonUtil.convertObjToStr(txtAcHd.getText());
            oldValue = CommonUtil.convertObjToStr(tblData.getValueAt(0, 2));
            
            if (rdoCash.isSelected()) {
                transMode = "CASH";
            } else if (rdoTransfer.isSelected()) {
                transMode = "TRANSFER";
            }
            dataMap.put(CommonConstants.BRANCH_ID, branchCode);
            dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            dataMap.put("BATCH_ID", batchId);
            dataMap.put("CORRECTION_TYPE", correctionType);
            dataMap.put("STATUS_BY", statusBy);
            dataMap.put("AUTHORIZED_BY", authorizedBy);
            dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
            dataMap.put("AUTH_STAFF", approvalUserId);
            dataMap.put("NEW_VALUE", newValue);
            dataMap.put("OLD_VALUE", oldValue);
            dataMap.put("ACT_NUM", actNum);
            dataMap.put("TRANS_MODE", transMode);
            dataMap.put("PROD_TYPE", prodType);
            dataMap.put("REMARKS", remarks);
            dataMap.put("AMOUNT", amount);
            dataMap.put("TRANS_TYPE", transType);
            try {
                HashMap resultMap = observable.doCorrectionProcess(dataMap);
                if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                    ClientUtil.showMessageWindow("Update Successful !!!");
                } else {
                    ClientUtil.showMessageWindow("Update Failed !!!");
                }
                btnFormClearActionPerformed(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void btnNarrationChangeSaveAction(String correctionType){ 
        if (txaNarration.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Particulars/Narration should not be empty !!!");
        } else {
            String newValue = "";
            String oldValue = "";
            String transMode = "";
            HashMap dataMap = new HashMap();
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
            System.out.println("branch code :: " + branchCode);
            System.out.println("correctionType :: " + correctionType);
            String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
            String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
            String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
            String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
            String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
            String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));

            DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
            System.out.println("user id :: " + objDataCorrectionApproveUI.getApprovalUserId());
            System.out.println("Remarks :: " + objDataCorrectionApproveUI.getTxtRemarks());
            if (objDataCorrectionApproveUI.isCancelActionKey()) {
                return;
            }
            String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
            String remarks = objDataCorrectionApproveUI.getTxtRemarks();
            if (correctionType.equals("NARRATION_CHANGE")) {
                newValue = CommonUtil.convertObjToStr(txaNarration.getText());
                oldValue = CommonUtil.convertObjToStr(tblData.getValueAt(0, 7));
            }else if (correctionType.equals("TRANS_NARRATION_CHANGE")) {
                newValue = CommonUtil.convertObjToStr(txaNarration.getText());
                oldValue = CommonUtil.convertObjToStr(tblData.getValueAt(0, 8));
            }
            if (rdoCash.isSelected()) {
                transMode = "CASH";
            } else if (rdoTransfer.isSelected()) {
                transMode = "TRANSFER";
            }
            dataMap.put(CommonConstants.BRANCH_ID, branchCode);
            dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            dataMap.put("BATCH_ID", batchId);
            dataMap.put("CORRECTION_TYPE", correctionType);
            dataMap.put("STATUS_BY", statusBy);
            dataMap.put("AUTHORIZED_BY", authorizedBy);
            dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
            dataMap.put("AUTH_STAFF", approvalUserId);
            dataMap.put("NEW_VALUE", newValue);
            dataMap.put("OLD_VALUE", oldValue);
            dataMap.put("ACT_NUM", actNum);
            dataMap.put("TRANS_MODE", transMode);
            dataMap.put("PROD_TYPE", prodType);
            dataMap.put("REMARKS", remarks);
            dataMap.put("AMOUNT", amount);
            dataMap.put("TRANS_TYPE",transType);
            try {
                HashMap resultMap = observable.doCorrectionProcess(dataMap);
                if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                    ClientUtil.showMessageWindow("Update Successful !!!");
                } else {
                    ClientUtil.showMessageWindow("Update Failed !!!");
                }
                btnFormClearActionPerformed(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
  
    
    private HashMap checkNegativeBal(String correctionType){
        HashMap balMap =  new HashMap();
        String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
        String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
        String oldProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductName.getModel()).getKeyForSelected());
        String oldProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        String newProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
        String newProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());
        double amount = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 5)); 
        Date transDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
        //getAllProductActNumBalance
        if(correctionType.equals("OA_ACTNO_CHANGE")){
          balMap.put("PROD_TYPE",oldProdType);
          balMap.put("PROD_ID",oldProdId);
          if(transType.equals("CREDIT")){
             balMap.put("ACT_NUM",txtAcctNo.getText());
             amount = -1*amount;
          }else{ 
             balMap.put("ACT_NUM",txtNewAcctNo.getText());  
             amount = -1*amount;
          }
          balMap.put("TRANS_TYPE",transType);
          balMap.put("AMOUNT",amount);
          balMap.put("TRANS_DT",transDt);
          balMap.put("CURR_DT",currDt);
          List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
          if(balanceList != null && balanceList.size() > 0){
              balMap = (HashMap)balanceList.get(0);
              balMap.put("NEGATIVE_BAL","NEGATIVE_BAL");
          }else{
              balMap.put("POSITIVE_BAL","POSITIVE_BAL");
          }
        }else if(correctionType.equals("SUSPENSE_ACTNO_CHANGE")){         
          if(transType.equals("CREDIT")){
             balMap.put("PROD_TYPE",oldProdType);
             balMap.put("PROD_ID",oldProdId);
             balMap.put("ACCT_NUM",txtAcctNo.getText());
             amount = -1*amount;
          }else{ 
             balMap.put("PROD_TYPE",newProdType);
             balMap.put("PROD_ID",newProdId); 
             balMap.put("ACCT_NUM",txtNewAcctNo.getText()); 
             amount = -1*amount;
          }
          balMap.put("TRANS_TYPE",transType);
          String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
          String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACCT_NUM"));
           boolean negYN = suspenseNegativeValue(checkingProdType,checkingActNum);
            if (!negYN) {
                balMap.put("AMOUNT", amount);
                balMap.put("TRANS_DT", transDt);
                balMap.put("CURR_DT", currDt);
                balMap.put("ACT_NUM",checkingActNum);
                List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                if (balanceList != null && balanceList.size() > 0) {
                    balMap = (HashMap) balanceList.get(0);
                    balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                } else {
                    balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                }
            }
        }
        
        return balMap;
    }
    
    
    private HashMap checkNegativeBalOnTransAmountChange(String correctionType){
        HashMap balMap =  new HashMap();
        String transType = "";
        String prodType = "";
        String actNum = "";
        double amount = 0.0;
        double newAmount = 0.0;
        double changeAmt = 0.0; 
        String prodId = "";
        Date transDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
        for(int i=0 ; i<tblData.getRowCount(); i++){
            transType = CommonUtil.convertObjToStr(tblData.getValueAt(i, 3));
            prodType = CommonUtil.convertObjToStr(tblData.getValueAt(i, 14));
            actNum = CommonUtil.convertObjToStr(tblData.getValueAt(i, 4));
            prodId = CommonUtil.convertObjToStr(tblData.getValueAt(i, 9));
            amount = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 5)); 
            newAmount = CommonUtil.convertObjToDouble(txtAmt.getText());
            if (prodType.equals("OA")) {
                balMap.put("ACT_NUM", actNum);
                balMap.put("TRANS_DT", transDt);
                if (transType.equals("CREDIT")) {
                    if (newAmount < amount) {
                        changeAmt = newAmount - amount;
                        balMap.put("AMOUNT", changeAmt);
                        List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                        if (balanceList != null && balanceList.size() > 0) {
                            balMap = (HashMap) balanceList.get(0);
                            balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                            break;
                        }
                    }

                } else if (transType.equals("DEBIT")) {
                    if (newAmount > amount) {
                        changeAmt = amount - newAmount;
                        balMap.put("AMOUNT", changeAmt);
                        List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                        if (balanceList != null && balanceList.size() > 0) {
                            balMap = (HashMap) balanceList.get(0);
                            balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                            break;
                        }
                    }
                }
                
            }else if (prodType.equals("SA")) {
                boolean negYN = suspenseNegativeValue(prodId,actNum);
                balMap.put("ACT_NUM", actNum);
                balMap.put("TRANS_DT", transDt);
                if (transType.equals("CREDIT")) {
                    if (newAmount < amount) {
                        changeAmt = newAmount - amount;
                        balMap.put("AMOUNT", changeAmt);
                        if (!negYN) {
                            balMap.put("TRANS_DT", transDt);
                            balMap.put("CURR_DT", currDt);
                            List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                            if (balanceList != null && balanceList.size() > 0) {
                                balMap = (HashMap) balanceList.get(0);
                                balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                                break;
                            }
                        }
                    }                    
                } else if (transType.equals("DEBIT")) {
                    if (newAmount > amount) {
                        changeAmt = amount - newAmount;
                        balMap.put("AMOUNT", changeAmt);
                        if (!negYN) {
                            balMap.put("TRANS_DT", transDt);
                            balMap.put("CURR_DT", currDt);
                            List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                            if (balanceList != null && balanceList.size() > 0) {
                                balMap = (HashMap) balanceList.get(0);
                                balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                                break;
                            }
                        }
                    }                    
                }               
            }
        }        
        return balMap;
    }
    
    
     private HashMap checkNegativeBalOnTransTypeChange(HashMap transDataMap){
         System.out.println("transDataMap here :: " + transDataMap); 
        HashMap balMap =  new HashMap();
        String transType = "";
        String prodType = "";
        String transId = "";
        String actNum = "";
        double amount = 0.0;
        double newAmount = 0.0;
        double changeAmt = 0.0; 
        String prodId = "";
        Date transDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
        for(int i=0 ; i<tblData.getRowCount(); i++){
            transId = CommonUtil.convertObjToStr(tblData.getValueAt(i, 0));
            transType = CommonUtil.convertObjToStr(transDataMap.get(transId));            
            prodType = CommonUtil.convertObjToStr(tblData.getValueAt(i, 14));
            System.out.println("transType :: " + transType +" trans id :: " + transId +" Prodtype :: " + prodType);
            actNum = CommonUtil.convertObjToStr(tblData.getValueAt(i, 4));
            prodId = CommonUtil.convertObjToStr(tblData.getValueAt(i, 9));
            amount = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 5)); 
            newAmount = CommonUtil.convertObjToDouble(txtAmt.getText());
            if (prodType.equals("OA")) {
                balMap.put("ACT_NUM", actNum);
                balMap.put("TRANS_DT", transDt);
               if (transType.equalsIgnoreCase("DEBIT")) {
                        changeAmt = -2 * amount;
                        balMap.put("AMOUNT", changeAmt);
                        List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                        if (balanceList != null && balanceList.size() > 0) {
                            balMap = (HashMap) balanceList.get(0);
                            balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                            break;
                        }                    
                }
                
            }else if (prodType.equals("SA")) {
                boolean negYN = suspenseNegativeValue(prodId,actNum);
                balMap.put("ACT_NUM", actNum);
                balMap.put("TRANS_DT", transDt);
                if (transType.equalsIgnoreCase("DEBIT")) {
                        changeAmt = -2 * amount;
                        balMap.put("AMOUNT", changeAmt);
                        if (!negYN) {
                            balMap.put("TRANS_DT", transDt);
                            balMap.put("CURR_DT", currDt);
                            List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                            if (balanceList != null && balanceList.size() > 0) {
                                balMap = (HashMap) balanceList.get(0);
                                balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                                break;
                            }
                        }                                        
                }               
            }
        }        
        return balMap;
    }
    
    private void btnOperativeAccountNoSaveAction(String correctionType){ 
        if (txtNewAcctNo.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Account No. should not be empty !!!");
        }else if (txtNewAcctNo.getText().equals(txtAcctNo.getText())) {
            ClientUtil.showMessageWindow("New Account No and Old Act No should not be Same !!!");
        } else {
            HashMap balMap = checkNegativeBal(correctionType);
            if (balMap.containsKey("NEGATIVE_BAL") && balMap.get("NEGATIVE_BAL") != null && balMap.get("NEGATIVE_BAL").equals("NEGATIVE_BAL")) {
                ClientUtil.showMessageWindow("Correction to Acct No." + CommonUtil.convertObjToStr(balMap.get("ACT_NUM")) + "\n will result in negative "
                        + "balance\n on"+DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balMap.get("TRANS_DT"))))
                        +"of amount "+ CommonUtil.convertObjToDouble(balMap.get("BALANCE"))+"\nData Correction not possible !!! "
                        );
            }else{
                String newValue = "";
                String oldValue = "";
                String transMode = "";
                String newAcctBranchCode = "";
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
                System.out.println("branch code :: " + branchCode);
                System.out.println("correctionType :: " + correctionType);
                String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
                String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
                String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
                String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));

                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                System.out.println("user id :: " + objDataCorrectionApproveUI.getApprovalUserId());
                System.out.println("Remarks :: " + objDataCorrectionApproveUI.getTxtRemarks());
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();
                newValue = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                oldValue = actNum;

                if (rdoCash.isSelected()) {
                    transMode = "CASH";
                } else if (rdoTransfer.isSelected()) {
                    transMode = "TRANSFER";
                }
                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                // Check for interbranch
                HashMap interBranchCodeMap = new HashMap();
                interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
                List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                    newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                }
                if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                    dataMap.put("INTERBRANCH_ACCT", "Y");
                } else {
                    dataMap.put("INTERBRANCH_ACCT", "N");
                }
                System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
                dataMap.put("NEW_ACT_BRANCH_ID", newAcctBranchCode);
                //End
                dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
                dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
                dataMap.put("BATCH_ID", batchId);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);
                dataMap.put("NEW_VALUE", newValue);
                dataMap.put("OLD_VALUE", oldValue);
                dataMap.put("ACT_NUM", actNum);
                dataMap.put("TRANS_MODE", transMode);
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("REMARKS", remarks);
                dataMap.put("AMOUNT", amount);
                dataMap.put("TRANS_TYPE", transType);
                try {
                    HashMap resultMap = observable.doAcctNoChangeCorrectionProcess(dataMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
        }
    }
    
     private void btnTransAmtChangeSaveAction(String correctionType){ 
        double transAmount = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 5)); 
        if (txtAmt.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Amount should not be empty !!!");
        }else if(txtAmt.getText().length() > 0 && CommonUtil.convertObjToDouble(txtAmt.getText()) <= 0){
            ClientUtil.showMessageWindow("Amount should be greater than zero !!!");
            txtAmt.setText("");
        }else if(transAmount == CommonUtil.convertObjToDouble(txtAmt.getText())){
            ClientUtil.showMessageWindow("Amount should not be same as old transaction amount !!!");
            txtAmt.setText("");
        }else {
            HashMap balMap = checkNegativeBalOnTransAmountChange(correctionType);
            if (balMap.containsKey("NEGATIVE_BAL") && balMap.get("NEGATIVE_BAL") != null && balMap.get("NEGATIVE_BAL").equals("NEGATIVE_BAL")) {
                ClientUtil.showMessageWindow("Correction to Acct No." + CommonUtil.convertObjToStr(balMap.get("ACT_NUM")) + "\n will result in negative "
                        + "balance\n on"+DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balMap.get("TRANS_DT"))))
                        +"of amount "+ CommonUtil.convertObjToDouble(balMap.get("BALANCE"))+"\nData Correction not possible !!! "
                        );
            }else{
                String newValue = "";
                String oldValue = "";
                String transMode = "";
                String newAcctBranchCode = "";
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
                System.out.println("branch code :: " + branchCode);
                System.out.println("correctionType :: " + correctionType);
                String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
                String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
                String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
                String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));

                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                System.out.println("user id :: " + objDataCorrectionApproveUI.getApprovalUserId());
                System.out.println("Remarks :: " + objDataCorrectionApproveUI.getTxtRemarks());
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();
                newValue = CommonUtil.convertObjToStr(txtAmt.getText());
                oldValue = amount;

                if (rdoCash.isSelected()) {
                    transMode = "CASH";
                } else if (rdoTransfer.isSelected()) {
                    transMode = "TRANSFER";
                }
                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                // Check for interbranch
                HashMap interBranchCodeMap = new HashMap();
                interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
                List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                    newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                }
                if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                    dataMap.put("INTERBRANCH_ACCT", "Y");
                } else {
                    dataMap.put("INTERBRANCH_ACCT", "N");
                }
                System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
                dataMap.put("NEW_ACT_BRANCH_ID", newAcctBranchCode);
                //End
                dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
                dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
                dataMap.put("BATCH_ID", batchId);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);
                dataMap.put("NEW_VALUE", newValue);
                dataMap.put("OLD_VALUE", oldValue);
                dataMap.put("ACT_NUM", actNum);
                dataMap.put("TRANS_MODE", transMode);
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("REMARKS", remarks);
                dataMap.put("AMOUNT", amount);
                dataMap.put("TRANS_TYPE", transType);
                try {
                    HashMap resultMap = observable.doTransAmtChangeCorrectionProcess(dataMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
        }
    }
     
    private boolean checkBatchTallyOrNot(){
        boolean tally = true;
        if(cboNewTransType1.getSelectedItem().equals(cboNewTransType2.getSelectedItem())){
            tally = false;
        }
        return tally;
    }
     
     private void btnTransTypeInterChangeSaveAction(String correctionType){ 
        if(cboNewTransType1.getSelectedIndex() == 0 || cboNewTransType2.getSelectedIndex() == 0){
            ClientUtil.showMessageWindow("New Transaction Type Should not be empty !!!");
        }else if(!checkBatchTallyOrNot()){
            ClientUtil.showMessageWindow("Transaction batch not tallied !!!");
        }else {
            HashMap transDataMap =  new HashMap();
            transDataMap.put(lblTransId1.getText(),cboNewTransType1.getSelectedItem());
            transDataMap.put(lblTransId2.getText(),cboNewTransType2.getSelectedItem());
            String newValue = lblTransId1.getText()+"-"+cboNewTransType1.getSelectedItem()+","+lblTransId2.getText()+"-"+cboNewTransType2.getSelectedItem();
            String oldValue = lblTransId1.getText()+"-"+cboNewTransType2.getSelectedItem()+","+lblTransId2.getText()+"-"+cboNewTransType1.getSelectedItem();
            HashMap balMap = checkNegativeBalOnTransTypeChange(transDataMap);
            if (balMap.containsKey("NEGATIVE_BAL") && balMap.get("NEGATIVE_BAL") != null && balMap.get("NEGATIVE_BAL").equals("NEGATIVE_BAL")) {
                ClientUtil.showMessageWindow("Correction to Acct No." + CommonUtil.convertObjToStr(balMap.get("ACT_NUM")) + "\n will result in negative "
                        + "balance\n on"+DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balMap.get("TRANS_DT"))))
                        +"of amount "+ CommonUtil.convertObjToDouble(balMap.get("BALANCE"))+"\nData Correction not possible !!! "
                        );
            }else{
             
                String transMode = "";
                String newAcctBranchCode = "";
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
                System.out.println("branch code :: " + branchCode);
                System.out.println("correctionType :: " + correctionType);
                String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
                String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
                String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
                String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));

                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                System.out.println("user id :: " + objDataCorrectionApproveUI.getApprovalUserId());
                System.out.println("Remarks :: " + objDataCorrectionApproveUI.getTxtRemarks());
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();
               

                if (rdoCash.isSelected()) {
                    transMode = "CASH";
                } else if (rdoTransfer.isSelected()) {
                    transMode = "TRANSFER";
                }
                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                // Check for interbranch
                HashMap interBranchCodeMap = new HashMap();
                interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
                List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                    newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                }
                if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                    dataMap.put("INTERBRANCH_ACCT", "Y");
                } else {
                    dataMap.put("INTERBRANCH_ACCT", "N");
                }
                System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
                dataMap.put("NEW_ACT_BRANCH_ID", newAcctBranchCode);
                //End
                dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
                dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
                dataMap.put("BATCH_ID", batchId);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);               
                dataMap.put("ACT_NUM", actNum);
                dataMap.put("TRANS_MODE", transMode);
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("REMARKS", remarks);
                dataMap.put("AMOUNT", amount);
                dataMap.put("NEW_VALUE", newValue);
                dataMap.put("OLD_VALUE", oldValue);
                dataMap.put("TRANS_TYPE", transType);
                dataMap.put("TRANS_ID_MAP",transDataMap);
                try {
                    HashMap resultMap = observable.doTransTypeInterChangeCorrectionProcess(dataMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
        }
    } 
     
     
    
    private void btnSuspenseAccountNoSaveAction(String correctionType){ 
        if (cboNewProdId.getSelectedIndex() == 0) {
            ClientUtil.showMessageWindow("New Suspense Prod Id should not be empty !!!");
        }else if (txtNewAcctNo.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Account No. should not be empty !!!");
        } else if (txtNewAcctNo.getText().equals(txtAcctNo.getText())) {
            ClientUtil.showMessageWindow("New Account No and Old Act No should not be Same !!!");
        }else {
            HashMap balMap = checkNegativeBal(correctionType);
            if (balMap.containsKey("NEGATIVE_BAL") && balMap.get("NEGATIVE_BAL") != null && balMap.get("NEGATIVE_BAL").equals("NEGATIVE_BAL")) {
                ClientUtil.showMessageWindow("Correction to Acct No." + CommonUtil.convertObjToStr(balMap.get("ACT_NUM")) + "\n will result in negative "
                        + "balance\n on"+DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balMap.get("TRANS_DT"))))
                        +"of amount "+ CommonUtil.convertObjToDouble(balMap.get("BALANCE"))+"\nData Correction not possible !!! "
                        );
            }else{
            String newValue = "";
            String oldValue = "";
            String transMode = "";
            String newAcctBranchCode = "";
            HashMap dataMap = new HashMap();
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
            System.out.println("branch code :: " + branchCode);
            System.out.println("correctionType :: " + correctionType);
            String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
            String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
            String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
            String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
            String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
            String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
            String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));

            DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
            System.out.println("user id :: " + objDataCorrectionApproveUI.getApprovalUserId());
            System.out.println("Remarks :: " + objDataCorrectionApproveUI.getTxtRemarks());
            if (objDataCorrectionApproveUI.isCancelActionKey()) {
                return;
            }
            String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
            String remarks = objDataCorrectionApproveUI.getTxtRemarks();
            newValue = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
            oldValue = actNum;
            
            if (rdoCash.isSelected()) {
                transMode = "CASH";
            } else if (rdoTransfer.isSelected()) {
                transMode = "TRANSFER";
            }
            dataMap.put(CommonConstants.BRANCH_ID, branchCode);
            // Check for interbranch
            HashMap interBranchCodeMap = new HashMap();
            interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
            List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
            if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                interBranchCodeMap = (HashMap)interBranchCodeList.get(0);
                newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
            }
            if(!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)){
                dataMap.put("INTERBRANCH_ACCT", "Y");
            }else{
                dataMap.put("INTERBRANCH_ACCT", "N");
            }
            System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
            dataMap.put("NEW_ACT_BRANCH_ID",newAcctBranchCode);
            //End
            dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            dataMap.put("BATCH_ID", batchId);
            dataMap.put("CORRECTION_TYPE", correctionType);
            dataMap.put("STATUS_BY", statusBy);
            dataMap.put("AUTHORIZED_BY", authorizedBy);
            dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
            dataMap.put("AUTH_STAFF", approvalUserId);
            dataMap.put("NEW_VALUE", newValue);
            dataMap.put("OLD_VALUE", oldValue);
            dataMap.put("ACT_NUM", actNum);
            dataMap.put("TRANS_MODE", transMode);
            dataMap.put("PROD_TYPE", prodType);
            dataMap.put("REMARKS", remarks);
            dataMap.put("AMOUNT", amount);
            dataMap.put("TRANS_TYPE",transType);
            try {
                HashMap resultMap = observable.doAcctNoChangeCorrectionProcess(dataMap);
                if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                    ClientUtil.showMessageWindow("Update Successful !!!");
                } else {
                    ClientUtil.showMessageWindow("Update Failed !!!");
                }
                btnFormClearActionPerformed(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
         } 
        }
    }
    
    
     private void btnOtherBankAccountNoSaveAction(String correctionType){ 
        if (txtNewAcctNo.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Account No. should not be empty !!!");
        }else if (txtNewAcctNo.getText().equals(txtAcctNo.getText())) {
            ClientUtil.showMessageWindow("New Account No and Old Act No should not be Same !!!");
        } else {
            String newValue = "";
            String oldValue = "";
            String transMode = "";
            String newAcctBranchCode = "";
            HashMap dataMap = new HashMap();
            String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
            String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
            System.out.println("branch code :: " + branchCode);
            System.out.println("correctionType :: " + correctionType);
            String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
            String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
            String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
            String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
            String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
            String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
            String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));

            DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
            System.out.println("user id :: " + objDataCorrectionApproveUI.getApprovalUserId());
            System.out.println("Remarks :: " + objDataCorrectionApproveUI.getTxtRemarks());
            if (objDataCorrectionApproveUI.isCancelActionKey()) {
                return;
            }
            String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
            String remarks = objDataCorrectionApproveUI.getTxtRemarks();
            newValue = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
            oldValue = actNum;
            
            if (rdoCash.isSelected()) {
                transMode = "CASH";
            } else if (rdoTransfer.isSelected()) {
                transMode = "TRANSFER";
            }
            dataMap.put(CommonConstants.BRANCH_ID, branchCode);
            // Check for interbranch
            HashMap interBranchCodeMap = new HashMap();
            interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
            List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
            if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                interBranchCodeMap = (HashMap)interBranchCodeList.get(0);
                newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
            }
            if(!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)){
                dataMap.put("INTERBRANCH_ACCT", "Y");
            }else{
                dataMap.put("INTERBRANCH_ACCT", "N");
            }
            System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
            dataMap.put("NEW_ACT_BRANCH_ID",newAcctBranchCode);
            //End
            HashMap acHeads = new HashMap();
            acHeads.put("INVESTMENT_ACC_NO", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
            List acHdLst  = ClientUtil.executeQuery("getOthrBankAccountAcHdAndProdId", acHeads);
            if(acHdLst != null && acHdLst.size() > 0){
              acHeads = (HashMap) acHdLst.get(0);
              dataMap.put("AC_HD_ID", CommonUtil.convertObjToStr(acHeads.get("PRINCIPAL_AC_HD")));
              dataMap.put("OTHER_BANK_PROD_ID",CommonUtil.convertObjToStr(acHeads.get("PROD_ID")));
            }
            
            dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
            dataMap.put("BATCH_ID", batchId);
            dataMap.put("CORRECTION_TYPE", correctionType);
            dataMap.put("STATUS_BY", statusBy);
            dataMap.put("AUTHORIZED_BY", authorizedBy);
            dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
            dataMap.put("AUTH_STAFF", approvalUserId);
            dataMap.put("NEW_VALUE", newValue);
            dataMap.put("OLD_VALUE", oldValue);
            dataMap.put("ACT_NUM", actNum);
            dataMap.put("TRANS_MODE", transMode);
            dataMap.put("PROD_TYPE", prodType);
            dataMap.put("REMARKS", remarks);
            dataMap.put("AMOUNT", amount);
            dataMap.put("TRANS_TYPE",transType);
            try {
                HashMap resultMap = observable.doAcctNoChangeCorrectionProcess(dataMap);
                if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                    ClientUtil.showMessageWindow("Update Successful !!!");
                } else {
                    ClientUtil.showMessageWindow("Update Failed !!!");
                }
                btnFormClearActionPerformed(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    private HashMap getGoldRate() {
        HashMap goldRateMap = new HashMap();
        double perGramAmt = 0.0;
        double eligibleAmt = 0.0;
        HashMap purityMap = new HashMap();
        String purity = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));
        perGramAmt = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 6));
        purityMap.put("PURITY", purity);
        purityMap.put("TODAY_DATE", currDt);
        if (purity.length() > 0) {
            goldRateMap = calcEligibleLoanAmount(perGramAmt);
        }
       
        return goldRateMap;
    }
    
    
    
     private HashMap calcEligibleLoanAmount(double perGramAmt) {
         HashMap goldRateMap = new HashMap();
         System.out.println("perGramAmt :: " + perGramAmt);
         /*
         perGramAmt :: 4900.0
margin :: 10.0
totSecurityValue :: 19600.0
totMarginAmt :: 1960.0
goldRateMap here ::  {OLD_GROSS_WEIGT=3.5, NEW_NET_WEIGHT=4, SECURITY_VALUE=19600.0, NEW_GROSS_WEIGHT=4, ELIGIBLE_AMT=17640.0, MARGIN_AMT=1960.0, OLD_NET_WEIGHT=3.5}
eligible amount :: 17640.0

         */
        double margin = 0.0; 
        double totEligibleAmt = 0.0;
        String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        LinkedHashMap whereMap = new LinkedHashMap();
        HashMap marginMap =  new HashMap();
        marginMap.put("PROD_ID", prodId);
        whereMap.put("PROD_ID", prodId);
        margin = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 7));
        System.out.println("margin :: " + margin);         
        String marketRate = String.valueOf(perGramAmt); 
        double totSecurityValue = perGramAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
         System.out.println("totSecurityValue :: " + totSecurityValue);
        double totMarginAmt = ((margin * totSecurityValue)) / 100;
         System.out.println("totMarginAmt :: " + totMarginAmt);
        HashMap singleAuthorizeMapOpBal = new HashMap();
        singleAuthorizeMapOpBal.put("PROD_ID", prodId);
        singleAuthorizeMapOpBal.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        List aListOp = ClientUtil.executeQuery("getFixedOrMarket", singleAuthorizeMapOpBal);
        String isFixedOrMarket = "N";
        double fixedRate = 0;
        if (aListOp.size() > 0 && aListOp.get(0) != null) {
            HashMap mapop = (HashMap) aListOp.get(0);
            isFixedOrMarket = CommonUtil.convertObjToStr(mapop.get("BY_MARGIN_FIXED"));
            fixedRate = CommonUtil.convertObjToDouble(mapop.get("FIXED_RATE_GOLD"));
        }
        if (isFixedOrMarket != null && isFixedOrMarket.equalsIgnoreCase("Y")) {
            totMarginAmt = fixedRate;
            totEligibleAmt = totMarginAmt * CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
            totEligibleAmt = (double) getNearest((long) (totEligibleAmt * 100), 100) / 100;
        } else {
            totEligibleAmt = totSecurityValue - totMarginAmt;
        }
        totEligibleAmt = (double) getNearest((long) (totEligibleAmt * 100), 100) / 100;
        goldRateMap.put("ELIGIBLE_AMT",totEligibleAmt);
        goldRateMap.put("SECURITY_VALUE",totSecurityValue);
        goldRateMap.put("MARGIN_AMT", totMarginAmt);
        goldRateMap.put("OLD_GROSS_WEIGT", CommonUtil.convertObjToDouble(tblData.getValueAt(0, 3)));
        goldRateMap.put("OLD_NET_WEIGHT", CommonUtil.convertObjToDouble(tblData.getValueAt(0, 4)));
        goldRateMap.put("NEW_GROSS_WEIGHT", CommonUtil.convertObjToDouble(txtGrossWeight.getText()));
        goldRateMap.put("NEW_NET_WEIGHT", CommonUtil.convertObjToDouble(txtNetWeight.getText()));
        return goldRateMap;
    }
     
     
     
    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

     
     public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
        if ((mod <= (roundingFactor / 2)) || (mod <= (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    
     
     private void btnGoldWeightChangeSaveAction(String correctionType){
        if (txtNetWeight.getText().length() == 0) {
            ClientUtil.showMessageWindow("Net Weight should not be empty !!!");
        } else if (txtGrossWeight.getText().length() == 0) {
            ClientUtil.showMessageWindow("Gross Weight should not be empty !!!");
        } else{
            HashMap goldRateMap  = getGoldRate();
            System.out.println("goldRateMap here ::  " + goldRateMap);
            double eligibleAmt = CommonUtil.convertObjToDouble(goldRateMap.get("ELIGIBLE_AMT"));
            System.out.println("eligible amount :: " + eligibleAmt);
            double limit = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 2));
            if (eligibleAmt < limit) {
                ClientUtil.showMessageWindow("Eligible Amt less than limit .\nWeight chnage not possible !!!");
            } else {
                String newValue = "";
                String oldValue = "";
                String transMode = "";
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                System.out.println("branch code :: " + branchCode);
                System.out.println("correctionType :: " + correctionType);
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 0));
                String oldGrossWt = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4)); 
                String oldNetWt = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5)); 
                String newGrossWt = CommonUtil.convertObjToStr(txtGrossWeight.getText()); 
                String newNetWt = CommonUtil.convertObjToStr(txtNetWeight.getText()); 
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 11));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
                String securityValue = CommonUtil.convertObjToStr(goldRateMap.get("SECURITY_VALUE"));
                String marginAmt = CommonUtil.convertObjToStr(goldRateMap.get("MARGIN_AMT"));               
                
               
                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();               

                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);
                dataMap.put("REMARKS", remarks);
                dataMap.put("ACT_NUM", actNum);
                dataMap.put("GOLD_DATA_MAP",goldRateMap);
                try {
                    HashMap resultMap = observable.doGoldWeightCorrectionProcess(dataMap);
                    System.out.println("resultMap :: " + resultMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!! \n Print Gold Bond !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
     }
     
     private void btnGoldItemChangeSaveAction(String correctionType){
        if (txtGoldItems.getText().length() == 0) {
            ClientUtil.showMessageWindow("Gold Items should not be empty !!!");
        } else{
                
               
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                System.out.println("branch code :: " + branchCode);
                System.out.println("correctionType :: " + correctionType);
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 0));
                String olditems = CommonUtil.convertObjToStr(tblData.getValueAt(0, 10));
                String newitems = txtGoldItems.getText();
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 11));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));   
               
                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();               

                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("NEW_VALUE", newitems);
                dataMap.put("OLD_VALUE", olditems);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);
                dataMap.put("REMARKS", remarks);
                dataMap.put("ACT_NUM", actNum);
                try {
                    HashMap resultMap = observable.doGoldItemCorrectionProcess(dataMap);
                    System.out.println("resultMap :: " + resultMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!! \n Print Gold Bond !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            
        }
     }
     
     private boolean suspenseNegativeValue(String prodType, String accNo){
          boolean negYn = false;
          HashMap wheMap = new HashMap();
            wheMap.put("PRODUCT_ID", prodType);
            wheMap.put("ACCT_NUM", accNo);
            List lt1 = ClientUtil.executeQuery("getNegativeAmtCheckForSA", wheMap);
            if (lt1 != null && lt1.size() > 0) {
                HashMap tMap = (HashMap) lt1.get(0);
                if(CommonUtil.convertObjToStr(tMap.get("NEG_AMT_YN")).equals("Y")){
                    negYn = true;
                }
            }          
          return negYn;          
      }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
         //HashMap resultMap = observable.renewAccounts();
        
        String correctionType =  CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
        if(tblData.getRowCount() == 0){
            ClientUtil.showMessageWindow("Existing data not selected !!!");
        }else if (correctionType.equals("NARRATION_CHANGE") || correctionType.equals("TRANS_NARRATION_CHANGE")){
            btnNarrationChangeSaveAction(correctionType);
        }else if(correctionType.equals("HEAD_CHANGE")){
            btnHeadChangeSaveAction(correctionType);
        }else if(correctionType.equals("GOLD_WEIGHT_CHANGE")){
            btnGoldWeightChangeSaveAction(correctionType);
        }else if(correctionType.equals("GOLD_ITEM_CHANGE")){
            btnGoldItemChangeSaveAction(correctionType);
        }else if(correctionType.equals("OA_ACTNO_CHANGE")){
            btnOperativeAccountNoSaveAction(correctionType);
        }else if(correctionType.equals("SUSPENSE_ACTNO_CHANGE")){
            btnSuspenseAccountNoSaveAction(correctionType);
        }else if(correctionType.equals("OTHERBANK_ACTNO_CHANGE")){
            btnOtherBankAccountNoSaveAction(correctionType);
        }else if(correctionType.equals("TRANS_AMT_CHANGE")){
            btnTransAmtChangeSaveAction(correctionType);
        } else if(correctionType.equals("TRANS_TYPE_INTERCHANGE")){
            btnTransTypeInterChangeSaveAction(correctionType);
        }else if(correctionType.equals("OA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("SA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("AB_TO_ALL_ACTNO_CHANGE")){
            btnAllAccountNoSaveAction(correctionType);
        }else if(correctionType.equals("GL_TO_ACTNO_MAPPING")){
            btnGLToAllAccountNoSaveAction(correctionType);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void enableDisableTransIdSearch(boolean flag) {
        txtTransId.setEnabled(flag);
        btnTransId.setEnabled(flag);
        tdtDate.setEnabled(flag);
        cboTransType.setEnabled(flag);
        rdoCash.setEnabled(flag);
        rdoTransfer.setEnabled(flag);
    }
    
    
    private void CboCorrectionTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CboCorrectionTypeItemStateChanged
        // TODO add your handling code here:
        if(CboCorrectionType.getSelectedIndex() > 0){
            btnSearchClearActionPerformed(null);
            clearTableData();
            clearAccountNumberChangePanel();
            clearGoldItemChangePanel();
            clearTransTypeInterChangePanel();
            clearOtherChangePanel();
           String correctionType =  CommonUtil.convertObjToStr(((ComboBoxModel) CboCorrectionType.getModel()).getKeyForSelected());
           if(correctionType.equals("HEAD_CHANGE")){
               enableDisableOtherChangePanel(true);
               txtAmt.setEnabled(false);
               txaNarration.setEnabled(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
           }else if(correctionType.equals("GOLD_ITEM_CHANGE")){
               enableDisableOtherChangePanel(false);               
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(true);
               txtGrossWeight.setEnabled(false);
               txtNetWeight.setEnabled(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(false);
               cboProductName.setSelectedItem((observable.getCbmProductName()).getDataForKey("TL"));
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(true);
               btnAcctNo.setEnabled(true);
               if (!observable.getCbmNewProductName().getKeys().contains("TL")) {
                   observable.getCbmNewProductName().addKeyAndElement("TL", "Term Loans");
               }
           }else if(correctionType.equals("GOLD_WEIGHT_CHANGE")){
               enableDisableOtherChangePanel(false);               
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(true);
               txtGoldItems.setEnabled(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(false);
               cboProductName.setSelectedItem((observable.getCbmProductName()).getDataForKey("TL"));
               cboProductName.setEnabled(false);               
               cboProdId.setEnabled(true);
               btnAcctNo.setEnabled(true);
               if (!observable.getCbmNewProductName().getKeys().contains("TL")) {
                   observable.getCbmNewProductName().addKeyAndElement("TL", "Term Loans");
               }
           }else if(correctionType.equals("TRANS_TYPE_INTERCHANGE")){
               enableDisableOtherChangePanel(false);
               txaNarration.setEnabled(false);
               txtAcHd.setEnabled(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(true);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(false);
               rdoTransfer.setSelected(true);
           }else if(correctionType.equals("NARRATION_CHANGE")||correctionType.equals("TRANS_NARRATION_CHANGE")){
               enableDisableOtherChangePanel(true);
               txtAcHd.setEnabled(false);
               txtAmt.setEnabled(false);
               btnAcHd.setEnabled(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
           }else if(correctionType.equals("OA_ACTNO_CHANGE")){
               enableDisableOtherChangePanel(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
           }else if(correctionType.equals("SUSPENSE_ACTNO_CHANGE")){
               enableDisableOtherChangePanel(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
           }else if(correctionType.equals("OTHERBANK_ACTNO_CHANGE")){
               enableDisableOtherChangePanel(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
           }else if(correctionType.equals("TRANS_AMT_CHANGE")){
               enableDisableOtherChangePanel(true);
               txaNarration.setEnabled(false);
               txtAcHd.setEnabled(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(false);
               rdoTransfer.setSelected(true);
           } else if(correctionType.equals("OA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("SA_TO_ALL_ACTNO_CHANGE") || correctionType.equals("AB_TO_ALL_ACTNO_CHANGE")){
               enableDisableOtherChangePanel(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
               removeKeyElementsFromCombo();
               addGLFromCombo();
           }else if(correctionType.equals("GL_TO_ACTNO_MAPPING")){
               enableDisableOtherChangePanel(false);
               enableDisableAccountNumberChangePanel(false);
               enableDisableGoldItemChangePanel(false);
               enableDisableTransTypeInterChangePanel(false);
               enableDisableTransIdSearch(true);
               cboProductName.setEnabled(false);
               cboProdId.setEnabled(false);
               btnAcctNo.setEnabled(false);
               rdoCash.setEnabled(true);
               removeKeyElementsFromCombo();
               removeGLFromCombo();
           }
           enableDisableTextFields(false);
        }
    }//GEN-LAST:event_CboCorrectionTypeItemStateChanged

    
    
    private void removeKeyElementsFromCombo(){
        
        if(observable.getCbmNewProductName().getKeys().contains("MDS")){
           observable.getCbmNewProductName().removeKeyAndElement("MDS"); 
        }
        if(observable.getCbmNewProductName().getKeys().contains("AD")){
           observable.getCbmNewProductName().removeKeyAndElement("AD"); 
        }
        if(observable.getCbmNewProductName().getKeys().contains("TL")){
           observable.getCbmNewProductName().removeKeyAndElement("TL"); 
        }
        if(observable.getCbmNewProductName().getKeys().contains("TD")){
           observable.getCbmNewProductName().removeKeyAndElement("TD"); 
        }
    }
    
     private void removeGLFromCombo(){        
        if(observable.getCbmNewProductName().getKeys().contains("GL")){
           observable.getCbmNewProductName().removeKeyAndElement("GL"); 
        }
    }
     
     private void addGLFromCombo(){        
        if(!observable.getCbmNewProductName().getKeys().contains("GL")){
           observable.getCbmNewProductName().addKeyAndElement("GL", "General Ledger");
        }
    } 
     
     
    private void rdoCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCashActionPerformed
        // TODO add your handling code here:
        if(rdoCash.isSelected()){
            rdoTransfer.setSelected(false);
        }else{
            rdoTransfer.setSelected(true);
            rdoCash.setSelected(false);
        }
         clearTableData();
    }//GEN-LAST:event_rdoCashActionPerformed

    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
        // TODO add your handling code here:
        if(tdtDate.getDateValue().length() > 0){
            Date maxBackTransDt = null;
            HashMap cbmsMap = new HashMap();
            List list;
            list = ClientUtil.executeQuery("getTillDateForDataCorrection", null);
            if (list != null && list.size() > 0) {
                cbmsMap = (HashMap) list.get(0);
                maxBackTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cbmsMap.get("CORRECTION_DATE")));
            }
            System.out.println("maxBackTransDt :: " + maxBackTransDt);            
            Date enteredDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
            System.out.println("enteredDt :: " + enteredDt);
            System.out.println("diff :: " + (DateUtil.dateDiff(maxBackTransDt, enteredDt)));
            
            if ((maxBackTransDt != null && (DateUtil.dateDiff(maxBackTransDt, enteredDt)) < 0)) {
                ClientUtil.displayAlert("Corrections allowed only\n From :" + DateUtil.getStringDate(maxBackTransDt) +"  To :" + DateUtil.getStringDate(currDt));
                tdtDate.setDateValue(null);
            }
        } 
    }//GEN-LAST:event_tdtDateFocusLost

    private void txtNetWeightFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetWeightFocusLost
        // TODO add your handling code here:
        double grossWeight = CommonUtil.convertObjToDouble(txtGrossWeight.getText()).doubleValue();
        double netWeight = CommonUtil.convertObjToDouble(txtNetWeight.getText()).doubleValue();
        if (grossWeight < netWeight) {
            ClientUtil.showAlertWindow("NetWeight should be less than grossweight");
            txtNetWeight.setText("");
            return;
        }
    }//GEN-LAST:event_txtNetWeightFocusLost

    private void cboNewTransType1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNewTransType1ItemStateChanged
        // TODO add your handling code here:
        if(tblData.getRowCount() == 0){
            ClientUtil.showMessageWindow("Existing data not displayed in grid !!!");
        }else{
        String transType = "";
        if (cboNewTransType1.getSelectedIndex() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                if (lblTransId1.getText().equals(tblData.getValueAt(i, 0))) {
                    transType = CommonUtil.convertObjToStr(tblData.getValueAt(i, 3));
                    break;
                }
            }
            if (transType.equalsIgnoreCase(CommonUtil.convertObjToStr(cboNewTransType1.getSelectedItem()))) {
                ClientUtil.showMessageWindow(lblTransId1.getText() + "having same trans type " + transType + "\nPlease change !!!");
                cboNewTransType1.setSelectedIndex(0);
            }
        }
       }
    }//GEN-LAST:event_cboNewTransType1ItemStateChanged

    private void cboNewTransType2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNewTransType2ItemStateChanged
        // TODO add your handling code here:
         if(tblData.getRowCount() == 0){
            ClientUtil.showMessageWindow("Existing data not displayed in grid !!!");
        }else{
        String transType = "";
        if (cboNewTransType2.getSelectedIndex() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                if (lblTransId2.getText().equals(tblData.getValueAt(i, 0))) {
                    transType = CommonUtil.convertObjToStr(tblData.getValueAt(i, 3));
                    break;
                }
            }
            if (transType.equalsIgnoreCase(CommonUtil.convertObjToStr(cboNewTransType2.getSelectedItem()))) {
                ClientUtil.showMessageWindow(lblTransId2.getText() + "having same trans type " + transType + "\nPlease change !!!");
                cboNewTransType2.setSelectedIndex(0);
            }
        }
       }
    }//GEN-LAST:event_cboNewTransType2ItemStateChanged

    private void txtAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToDouble(txtAmt.getText()) <= 0){
            ClientUtil.showMessageWindow("Amount should be greater than zero !!!");
        }
    }//GEN-LAST:event_txtAmtFocusLost

    private void cboNewProductNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboNewProductNameItemStateChanged
        // TODO add your handling code here:
       txtNewAcctNo.setText("");
    }//GEN-LAST:event_cboNewProductNameItemStateChanged

    private void cboTransTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTransTypeItemStateChanged
        // TODO add your handling code here:
        if(tblData.getRowCount() > 0)
           clearTableData();
    }//GEN-LAST:event_cboTransTypeItemStateChanged

    private void CboIndendCorrectionTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CboIndendCorrectionTypeItemStateChanged
        // TODO add your handling code here:
        txtIRId.setEnabled(false);
    }//GEN-LAST:event_CboIndendCorrectionTypeItemStateChanged

    private void tblIndendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIndendMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblIndendMouseClicked

    private void tblIndendMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIndendMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblIndendMousePressed

    private void tblIndendMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIndendMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblIndendMouseReleased

    private void tblIndendMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIndendMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblIndendMouseMoved

    private void btnIndendDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIndendDisplayActionPerformed
        // TODO add your handling code here:
        if (cboIndendBranchCode.getSelectedIndex() == 0) {
            ClientUtil.showMessageWindow("Select branch code !!!");
        } else if (CboIndendCorrectionType.getSelectedIndex() > 0) {
            if (tdtIndendTransDt.getDateValue().length() == 0) {
                ClientUtil.showMessageWindow("Enter transaction Date !!!");
            } else if (txtIRId.getText().length() == 0) {
                ClientUtil.showMessageWindow("IRID should not be empty !!!");
            } else {
                populateIndendData();
            }
        }
                
    }//GEN-LAST:event_btnIndendDisplayActionPerformed

    private void btnIndendSearchClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIndendSearchClearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIndendSearchClearActionPerformed

    private void btnIndendSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIndendSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIndendSaveActionPerformed

    private void btnIndendClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIndendClearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIndendClearActionPerformed

    private void btnIRIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIRIdSearchActionPerformed
        // TODO add your handling code here:
        if(cboIndendBranchCode.getSelectedIndex() == 0){
            ClientUtil.showMessageWindow("Select Branch !!!");
        } else if(CboIndendCorrectionType.getSelectedIndex() == 0){
           ClientUtil.showMessageWindow("Select Correction Type !!!"); 
        } else if(tdtIndendTransDt.getDateValue().length() == 0){
           ClientUtil.showMessageWindow("Select Indend Transaction Date !!!"); 
        }else{
            popUp("IR_ID");
        }
    }//GEN-LAST:event_btnIRIdSearchActionPerformed

    //Ver 2.0 changes
    
     private void btnAllAccountNoSaveAction(String correctionType){ 
        if (txtNewAcctNo.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Account No. should not be empty !!!");
        }else if (txtNewAcctNo.getText().equals(txtAcctNo.getText())) {
            ClientUtil.showMessageWindow("New Account No and Old Act No should not be Same !!!");
        } else {
            HashMap balMap = checkNegativeBalForAllActNoChange(correctionType);
            if (balMap.containsKey("NEGATIVE_BAL") && balMap.get("NEGATIVE_BAL") != null && balMap.get("NEGATIVE_BAL").equals("NEGATIVE_BAL")) {
                ClientUtil.showMessageWindow("Correction to Acct No." + CommonUtil.convertObjToStr(balMap.get("ACT_NUM")) + "\n will result in negative "
                        + "balance\n on"+DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balMap.get("TRANS_DT"))))
                        +"of amount "+ CommonUtil.convertObjToDouble(balMap.get("BALANCE"))+"\nData Correction not possible !!! "
                        );
            }else{
                String newValue = "";
                String oldValue = "";
                String transMode = "";
                String newAcctBranchCode = "";
                boolean newAcctInterbranch = false;
                boolean oldAcctInterbranch = false;
                String newProdType = "";
                String newProdId = "";
                String newAchdId = "";
                String newAcctNo = "";
                String oldProdType = "";
                String oldProdId = "";
                String oldAchdId = "";
                String oldAcctNo = "";
                
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
                
                String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
                String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
                String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 4));
                String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));
                
                
                oldProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductName.getModel()).getKeyForSelected());
                oldProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                oldAcctNo = CommonUtil.convertObjToStr(txtAcctNo.getText());
                
                newProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
                if (newProdType.equals("GL")) {
                    newAcctNo = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                } else {
                    newProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());
                    newAcctNo = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                }
                
                //Fetch Account Head                
                newAchdId = getNewAccountHeads(newProdType,newProdId,txtNewAcctNo.getText());
                oldAchdId = getNewAccountHeads(oldProdType,oldProdId,txtAcctNo.getText());
                
                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();
                newValue = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                oldValue = actNum;

                if (rdoCash.isSelected()) {
                    transMode = "CASH";
                } else if (rdoTransfer.isSelected()) {
                    transMode = "TRANSFER";
                }
                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                
                if (newProdType.equals("GL")) {
                    dataMap.put("NEW_ACT_BRANCH_ID", branchCode);
                } else {
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
                    List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                        System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                    }
                    if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                        newAcctInterbranch = true;
                    } 
                    System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
                    dataMap.put("NEW_ACT_BRANCH_ID", newAcctBranchCode);
                }
                
                HashMap interBranchCodeMap = new HashMap();
                interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcctNo.getText()));
                List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                    newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                    if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                        oldAcctInterbranch = true;
                    } 
                }
                if (newAcctInterbranch || oldAcctInterbranch) {
                    dataMap.put("INTERBRANCH_ACCT", "Y");
                } else {
                    dataMap.put("INTERBRANCH_ACCT", "N");
                }
                
                // Check for interbranch
               
                //End
                dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
                dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
                dataMap.put("BATCH_ID", batchId);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);
                dataMap.put("NEW_VALUE", newValue);
                dataMap.put("OLD_VALUE", oldValue);
                dataMap.put("ACT_NUM", actNum);
                dataMap.put("TRANS_MODE", transMode);
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("REMARKS", remarks);
                dataMap.put("AMOUNT", amount);
                dataMap.put("TRANS_TYPE", transType);
                
                //new and old values
                dataMap.put("OLD_PROD_TYPE", oldProdType);
                dataMap.put("OLD_PROD_ID", oldProdId);
                dataMap.put("OLD_ACT_NUM", oldAcctNo);
                dataMap.put("NEW_PROD_TYPE", newProdType);
                dataMap.put("NEW_PROD_ID", newProdId);
                dataMap.put("NEW_ACT_NUM", newAcctNo);
                dataMap.put("NEW_AC_HD_ID", newAchdId);
                dataMap.put("OLD_AC_HD_ID", oldAchdId);
                //End
                
                
                try {
                    HashMap resultMap = observable.doAllAcctNoChangeCorrectionProcess(dataMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
        }
    }
    
    
    private String getNewAccountHeads(String prodType, String prodId, String acctNum) {
        String acctHead = "";        
        HashMap headMap = new HashMap();
        if (prodType.equals("GL")) {
            acctHead = acctNum;
        } else if (prodType.equals("OA")) {
            headMap.put("ACT_NUM", acctNum);
            List lst = ClientUtil.executeQuery("getAccNoProdIdDet", headMap);
            if (lst != null && lst.size() > 0) {
                headMap = (HashMap) lst.get(0);
                acctHead = CommonUtil.convertObjToStr(headMap.get("AC_HD_ID"));
            }
        } else if (prodType.equals("SA")) {
            headMap.put("PROD_ID", prodId);
            List lst = ClientUtil.executeQuery("getSuspenseIntHead", headMap);
            if (lst != null && lst.size() > 0) {
                headMap = (HashMap) lst.get(0);
                acctHead = CommonUtil.convertObjToStr(headMap.get("AC_HD_ID"));
            }
        } else if (prodType.equals("AB")) {
            headMap.put("INVESTMENT_ACC_NO", acctNum);
            List lst = ClientUtil.executeQuery("getOthrBankAccountAcHdAndProdId", headMap);
            if (lst != null && lst.size() > 0) {
                headMap = (HashMap) lst.get(0);
                acctHead = CommonUtil.convertObjToStr(headMap.get("PRINCIPAL_AC_HD"));
            }
        }
        return acctHead;
    }

    
     private HashMap checkNegativeBalForAllActNoChange(String correctionType){
        HashMap balMap =  new HashMap();
        String oldProdId = "";
        String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
        String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
        String oldProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductName.getModel()).getKeyForSelected());
        if(cboProdId.getSelectedIndex() > 0){
          oldProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        }
        String newProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
        String newProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());
        double amount = CommonUtil.convertObjToDouble(tblData.getValueAt(0, 5)); 
        Date transDt = DateUtil.getDateMMDDYYYY(tdtDate.getDateValue());
        //getAllProductActNumBalance        
         if (correctionType.equals("OA_TO_ALL_ACTNO_CHANGE")) {
             boolean checking = true;
             if (!newProdType.equals("GL")) {
                 if (transType.equals("CREDIT")) {
                     balMap.put("PROD_TYPE", oldProdType);
                     balMap.put("PROD_ID", oldProdId);
                     balMap.put("ACT_NUM", txtAcctNo.getText());
                     amount = -1 * amount;
                     checking = true;
                 } else {
                     //System.out.println("execute here");
                     checking = true;
                     balMap.put("PROD_TYPE", newProdType);
                     balMap.put("PROD_ID", newProdId);
                     balMap.put("ACT_NUM", txtNewAcctNo.getText());
                     amount = -1 * amount;
                     if (newProdType.equals("SA")) {
                         String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
                         String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACT_NUM"));
                         boolean negYN = suspenseNegativeValue(checkingProdType, checkingActNum);
                         if (negYN) {
                             checking = false;
                         }
                     }
                 }
                 balMap.put("TRANS_TYPE", transType);
                 balMap.put("AMOUNT", amount);
                 balMap.put("TRANS_DT", transDt);
                 balMap.put("CURR_DT", currDt);
                 if (checking) {
                     List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                     if (balanceList != null && balanceList.size() > 0) {
                         balMap = (HashMap) balanceList.get(0);
                         balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                     } else {
                         balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                     }
                 }
             } else {     
                 
                checking = true;
                 if (transType.equals("CREDIT")) {
                     balMap.put("PROD_TYPE", oldProdType);
                     balMap.put("PROD_ID", oldProdId);
                     balMap.put("ACT_NUM", txtAcctNo.getText());
                     amount = -1 * amount;
                    if (newProdType.equals("SA")) {
                         String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
                         String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACT_NUM"));
                         boolean negYN = suspenseNegativeValue(checkingProdType, checkingActNum);
                         if (negYN) {
                             checking = false;
                         }
                     }
                 }
                 balMap.put("TRANS_TYPE", transType);
                 balMap.put("AMOUNT", amount);
                 balMap.put("TRANS_DT", transDt);
                 balMap.put("CURR_DT", currDt);
                 if (checking) {
                     List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                     if (balanceList != null && balanceList.size() > 0) {
                         balMap = (HashMap) balanceList.get(0);
                         balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                     } else {
                         balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                     }
                 }
             }
         }else if (correctionType.equals("SA_TO_ALL_ACTNO_CHANGE")) {
             if (!newProdType.equals("GL")) {
                 if (transType.equals("CREDIT")) {
                     balMap.put("PROD_TYPE", oldProdType);
                     balMap.put("PROD_ID", oldProdId);
                     balMap.put("ACT_NUM", txtAcctNo.getText());
                     amount = -1 * amount;
                 } else {
                     balMap.put("PROD_TYPE", newProdType);
                     balMap.put("PROD_ID", newProdId);
                     balMap.put("ACT_NUM", txtNewAcctNo.getText());
                     amount = -1 * amount;

                 }
                 balMap.put("TRANS_TYPE", transType);
                 balMap.put("AMOUNT", amount);
                 balMap.put("TRANS_DT", transDt);
                 balMap.put("CURR_DT", currDt);
                 String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
                 String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACT_NUM"));
                 boolean negYN = suspenseNegativeValue(checkingProdType, checkingActNum);
                 if (!negYN) {
                     List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                     if (balanceList != null && balanceList.size() > 0) {
                         balMap = (HashMap) balanceList.get(0);
                         balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                     } else {
                         balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                     }
                 }                 
             } else {
                 if (!newProdType.equals("GL")) {
                     if (transType.equals("CREDIT")) {
                         balMap.put("PROD_TYPE", oldProdType);
                         balMap.put("PROD_ID", oldProdId);
                         balMap.put("ACT_NUM", txtAcctNo.getText());
                         amount = -1 * amount;
                     }
                     balMap.put("TRANS_TYPE", transType);
                     balMap.put("AMOUNT", amount);
                     balMap.put("TRANS_DT", transDt);
                     balMap.put("CURR_DT", currDt);
                     String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
                     String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACT_NUM"));
                     boolean negYN = suspenseNegativeValue(checkingProdType, checkingActNum);
                     if (!negYN) {
                         List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                         if (balanceList != null && balanceList.size() > 0) {
                             balMap = (HashMap) balanceList.get(0);
                             balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                         } else {
                             balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                         }
                     }
                 }
             }
         } else if (correctionType.equals("AB_TO_ALL_ACTNO_CHANGE")) {
             boolean checking = true;
             if (!newProdType.equals("GL")) {
                 if (transType.equals("DEBIT")) {                   
                     balMap.put("PROD_TYPE", newProdType);
                     balMap.put("PROD_ID", newProdId);
                     balMap.put("ACT_NUM", txtNewAcctNo.getText());
                     amount = -1 * amount;
                     if (newProdType.equals("SA")) {
                         String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
                         String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACT_NUM"));
                         boolean negYN = suspenseNegativeValue(checkingProdType, checkingActNum);
                         if (negYN) {
                             checking = false;
                         }
                     }
                 }
                 balMap.put("TRANS_TYPE", transType);
                 balMap.put("AMOUNT", amount);
                 balMap.put("TRANS_DT", transDt);
                 balMap.put("CURR_DT", currDt);
                 if (checking) {
                     List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                     if (balanceList != null && balanceList.size() > 0) {
                         balMap = (HashMap) balanceList.get(0);
                         balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                     } else {
                         balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                     }
                 }
             } 
         }else if (correctionType.equals("GL_TO_ACTNO_MAPPING")) {
             boolean checking = true;
             if (transType.equals("DEBIT")) {
                 balMap.put("PROD_TYPE", oldProdType);
                 balMap.put("PROD_ID", oldProdId);
                 balMap.put("ACT_NUM", txtNewAcctNo.getText());
                 amount = -1 * amount;
                 balMap.put("TRANS_TYPE", transType);
                 balMap.put("AMOUNT", amount);
                 balMap.put("TRANS_DT", transDt);
                 balMap.put("CURR_DT", currDt);
                 if (newProdType.equals("SA")) {
                     String checkingProdType = CommonUtil.convertObjToStr(balMap.get("PROD_TYPE"));
                     String checkingActNum = CommonUtil.convertObjToStr(balMap.get("ACT_NUM"));
                     boolean negYN = suspenseNegativeValue(checkingProdType, checkingActNum);
                     if (negYN) {
                         checking = false;
                     }
                 }
                 if(checking){
                 List balanceList = ClientUtil.executeQuery("getAllProductActNumBalance", balMap);
                 if (balanceList != null && balanceList.size() > 0) {
                     balMap = (HashMap) balanceList.get(0);
                     balMap.put("NEGATIVE_BAL", "NEGATIVE_BAL");
                 } else {
                     balMap.put("POSITIVE_BAL", "POSITIVE_BAL");
                 }
                 }
             }
         }
        return balMap;
    }
    
    
    
    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    
    
       public void populateIndendData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        String mapName = "getSelectIRIDDetails";
        System.out.println("chkng");
        if (isOK) {
            String indendBranchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboIndendBranchCode.getModel()).getKeyForSelected());
            String indendCorrectionType = CommonUtil.convertObjToStr(((ComboBoxModel) CboIndendCorrectionType.getModel()).getKeyForSelected());
            String irId =  CommonUtil.convertObjToStr(txtIRId.getText());
            viewMap.put(CommonConstants.MAP_NAME, mapName);          
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtIndendTransDt.getDateValue()));
            whereMap.put("BRANCH_CODE", indendBranchCode);
            whereMap.put("IR_ID", irId);
            whereMap.put("CORRECTION_TYPE",indendCorrectionType);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateIndendTransData(viewMap, tblIndend);               
                panAward.setVisible(true);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    
    
    
     private void btnGLToAllAccountNoSaveAction(String correctionType){ 
        if (txtNewAcctNo.getText().length() == 0) {
            ClientUtil.showMessageWindow("New Account No. should not be empty !!!");
        }else if (txtNewAcctNo.getText().equals(txtAcctNo.getText())) {
            ClientUtil.showMessageWindow("New Account No and Old Act No should not be Same !!!");
        } else {
            HashMap balMap = checkNegativeBalForAllActNoChange(correctionType);
            if (balMap.containsKey("NEGATIVE_BAL") && balMap.get("NEGATIVE_BAL") != null && balMap.get("NEGATIVE_BAL").equals("NEGATIVE_BAL")) {
                ClientUtil.showMessageWindow("Correction to Acct No." + CommonUtil.convertObjToStr(balMap.get("ACT_NUM")) + "\n will result in negative "
                        + "balance\n on"+DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balMap.get("TRANS_DT"))))
                        +"of amount "+ CommonUtil.convertObjToDouble(balMap.get("BALANCE"))+"\nData Correction not possible !!! "
                        );
            }else{
                String newValue = "";
                String oldValue = "";
                String transMode = "";
                String newAcctBranchCode = "";
                boolean newAcctInterbranch = false;
                boolean oldAcctInterbranch = false;
                String newProdType = "";
                String newProdId = "";
                String newAchdId = "";
                String newAcctNo = "";
                String oldProdType = "";
                String oldProdId = "";
                String oldAchdId = "";
                String oldAcctNo = "";
                
                HashMap dataMap = new HashMap();
                String branchCode = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchCode.getModel()).getKeyForSelected());
                String transType = CommonUtil.convertObjToStr(((ComboBoxModel) cboTransType.getModel()).getKeyForSelected());
                
                String batchId = CommonUtil.convertObjToStr(tblData.getValueAt(0, 1));
                String statusBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 12));
                String authorizedBy = CommonUtil.convertObjToStr(tblData.getValueAt(0, 13));
                String prodType = CommonUtil.convertObjToStr(tblData.getValueAt(0, 14));
                String acctBranchCode = CommonUtil.convertObjToStr(tblData.getValueAt(0, 15));
                String actNum = CommonUtil.convertObjToStr(tblData.getValueAt(0, 2));
                String amount = CommonUtil.convertObjToStr(tblData.getValueAt(0, 5));
                
                
                oldProdType = "GL";
                oldAcctNo = CommonUtil.convertObjToStr(txtAcctNo.getText());
                
                newProdType = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProductName.getModel()).getKeyForSelected());
                if (newProdType.equals("GL")) {
                    newAcctNo = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                } else {
                    newProdId = CommonUtil.convertObjToStr(((ComboBoxModel) cboNewProdId.getModel()).getKeyForSelected());
                    newAcctNo = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                }
                
                //Fetch Account Head                
                newAchdId = getNewAccountHeads(newProdType,newProdId,txtNewAcctNo.getText());
                oldAchdId = getNewAccountHeads(oldProdType,oldProdId,txtAcctNo.getText());
                
                DataCorrectionApproveUI objDataCorrectionApproveUI = new DataCorrectionApproveUI(this);
                if (objDataCorrectionApproveUI.isCancelActionKey()) {
                    return;
                }
                String approvalUserId = objDataCorrectionApproveUI.getApprovalUserId();
                String remarks = objDataCorrectionApproveUI.getTxtRemarks();
                newValue = CommonUtil.convertObjToStr(txtNewAcctNo.getText());
                oldValue = actNum;

                if (rdoCash.isSelected()) {
                    transMode = "CASH";
                } else if (rdoTransfer.isSelected()) {
                    transMode = "TRANSFER";
                }
                dataMap.put(CommonConstants.BRANCH_ID, branchCode);
                
                if (newProdType.equals("GL")) {
                    dataMap.put("NEW_ACT_BRANCH_ID", branchCode);
                } else {
                    HashMap interBranchCodeMap = new HashMap();
                    interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtNewAcctNo.getText()));
                    List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                    if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                        interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                        newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                        System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                    }
                    if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                        newAcctInterbranch = true;
                    } 
                    System.out.println("newAcctBranchCode :: " + newAcctBranchCode);
                    dataMap.put("NEW_ACT_BRANCH_ID", newAcctBranchCode);
                }
                
                HashMap interBranchCodeMap = new HashMap();
                interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAcctNo.getText()));
                List interBranchCodeList = ClientUtil.executeQuery("getSelectInterBranchCode", interBranchCodeMap);
                if (interBranchCodeList != null && interBranchCodeList.size() > 0 && !CommonUtil.convertObjToStr(dataMap.get("PROD_TYPE")).equalsIgnoreCase("GL")) {
                    interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
                    newAcctBranchCode = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
                    System.out.println("newAcctBranchCode here  :: " + newAcctBranchCode);
                    if (!branchCode.equals(acctBranchCode) || !branchCode.equals(newAcctBranchCode)) {
                        oldAcctInterbranch = true;
                    } 
                }
                if (newAcctInterbranch || oldAcctInterbranch) {
                    dataMap.put("INTERBRANCH_ACCT", "Y");
                } else {
                    dataMap.put("INTERBRANCH_ACCT", "N");
                }
                
                // Check for interbranch
               
                //End
                dataMap.put("TRANS_DT", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
                dataMap.put("TRANS_ID", CommonUtil.convertObjToStr(txtTransId.getText()));
                dataMap.put("BATCH_ID", batchId);
                dataMap.put("CORRECTION_TYPE", correctionType);
                dataMap.put("STATUS_BY", statusBy);
                dataMap.put("AUTHORIZED_BY", authorizedBy);
                dataMap.put("CORRECTION_STAFF", ProxyParameters.USER_ID);
                dataMap.put("AUTH_STAFF", approvalUserId);
                dataMap.put("NEW_VALUE", newValue);
                dataMap.put("OLD_VALUE", oldValue);
                dataMap.put("ACT_NUM", actNum);
                dataMap.put("TRANS_MODE", transMode);
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("REMARKS", remarks);
                dataMap.put("AMOUNT", amount);
                dataMap.put("TRANS_TYPE", transType);
                
                //new and old values
                dataMap.put("OLD_PROD_TYPE", oldProdType);
                dataMap.put("OLD_PROD_ID", oldProdId);
                dataMap.put("OLD_ACT_NUM", oldAcctNo);
                dataMap.put("NEW_PROD_TYPE", newProdType);
                dataMap.put("NEW_PROD_ID", newProdId);
                dataMap.put("NEW_ACT_NUM", newAcctNo);
                dataMap.put("NEW_AC_HD_ID", newAchdId);
                dataMap.put("OLD_AC_HD_ID", oldAchdId);
                //End
                
                
                try {
                    HashMap resultMap = observable.doAllAcctNoChangeCorrectionProcess(dataMap);
                    if (resultMap.containsKey("STATUS") && resultMap.get("STATUS") != null && CommonUtil.convertObjToStr(resultMap.get("STATUS")).equals("SUCCESS")) {
                        ClientUtil.showMessageWindow("Update Successful !!!");
                    } else {
                        ClientUtil.showMessageWindow("Update Failed !!!");
                    }
                    btnFormClearActionPerformed(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  
        }
    }
    
    
    
    
    public void calTotalAmount() {
//        if (tblData.getRowCount() > 0) {
//            int count = tblData.getRowCount();
//            double selectedCount = 0.0;
//            for (int i = 0; i < count; i++) {
//                if ((Boolean) tblData.getValueAt(i, 0)) {
//                    selectedCount++;
//                }
//            }
//            lblSelectedRecords.setText(CommonUtil.convertObjToStr(selectedCount));
//        }
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
    private com.see.truetransact.uicomponent.CComboBox CboCorrectionType;
    private com.see.truetransact.uicomponent.CComboBox CboIndendCorrectionType;
    private com.see.truetransact.uicomponent.CButton btnAcHd;
    private com.see.truetransact.uicomponent.CButton btnAcctNo;
    private com.see.truetransact.uicomponent.CButton btnDepoSearch;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnFormClear;
    private com.see.truetransact.uicomponent.CButton btnIRIdSearch;
    private com.see.truetransact.uicomponent.CButton btnIndendClear;
    private com.see.truetransact.uicomponent.CButton btnIndendDisplay;
    private com.see.truetransact.uicomponent.CButton btnIndendSave;
    private com.see.truetransact.uicomponent.CButton btnIndendSearchClear;
    private com.see.truetransact.uicomponent.CButton btnNewAcctNo;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearchClear;
    private com.see.truetransact.uicomponent.CButton btnTransId;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel13;
    private com.see.truetransact.uicomponent.CLabel cLabel14;
    private com.see.truetransact.uicomponent.CLabel cLabel17;
    private com.see.truetransact.uicomponent.CLabel cLabel18;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboBranchCode;
    private com.see.truetransact.uicomponent.CComboBox cboIndendBranchCode;
    private com.see.truetransact.uicomponent.CComboBox cboNewProdId;
    private com.see.truetransact.uicomponent.CComboBox cboNewProductName;
    private com.see.truetransact.uicomponent.CComboBox cboNewTransType1;
    private com.see.truetransact.uicomponent.CComboBox cboNewTransType2;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProductName;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private javax.swing.JScrollPane jScrollPane1;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode1;
    private com.see.truetransact.uicomponent.CLabel lblCorrectionType;
    private com.see.truetransact.uicomponent.CLabel lblCorrectionType1;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblGoldItems;
    private com.see.truetransact.uicomponent.CLabel lblGoldItems2;
    private com.see.truetransact.uicomponent.CLabel lblHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblNewAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblNewDepo;
    private com.see.truetransact.uicomponent.CLabel lblNewPrductName;
    private com.see.truetransact.uicomponent.CLabel lblNewProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductName;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblTransId;
    private com.see.truetransact.uicomponent.CLabel lblTransId1;
    private com.see.truetransact.uicomponent.CLabel lblTransId2;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CDateField newTransDt;
    private com.see.truetransact.uicomponent.CPanel panAccountNoChange;
    private com.see.truetransact.uicomponent.CPanel panAward;
    private com.see.truetransact.uicomponent.CPanel panGoldStockChange;
    private com.see.truetransact.uicomponent.CPanel panGoldStockChange2;
    private com.see.truetransact.uicomponent.CPanel panIndendCorrections;
    private com.see.truetransact.uicomponent.CPanel panIndendIssueType;
    private com.see.truetransact.uicomponent.CPanel panIndendTable;
    private com.see.truetransact.uicomponent.CPanel panIssueType;
    private com.see.truetransact.uicomponent.CPanel panKCCREnewal;
    private com.see.truetransact.uicomponent.CPanel panOtherChange;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTranTypeChange;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CRadioButton rdoCash;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransfer;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcIndendTable;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblIndend;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CDateField tdtIndendTransDt;
    private com.see.truetransact.uicomponent.CTextArea txaNarration;
    private com.see.truetransact.uicomponent.CTextField txtAcHd;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtGoldItems;
    private com.see.truetransact.uicomponent.CTextField txtGrossWeight;
    private com.see.truetransact.uicomponent.CTextField txtIRId;
    private com.see.truetransact.uicomponent.CTextField txtLiabilityAmt;
    private com.see.truetransact.uicomponent.CTextField txtNetWeight;
    private com.see.truetransact.uicomponent.CTextField txtNewAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtNewDepo;
    private com.see.truetransact.uicomponent.CTextField txtTransId;
    // End of variables declaration//GEN-END:variables
}

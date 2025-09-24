/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ReleaseEnquiryUI.java
 *
 * Created on Augus 24, 2003, 1:46 PM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CDateField;

/**
 * @author balachandar
 */
public class ReleaseEnquiryUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
//    private final ViewAllRB resourceBundle = new ViewAllRB();

    private ReleaseEnquiryOB observable;
    HashMap paramMap = null;
    int amtColumnNo = 0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    boolean collDet = false;
    boolean StopPaymentView = false;
    private Date currDt = null;
    private final String PROD_ID = "PROD_ID";
//    private final static Logger log = Logger.getLogger(ViewAllTransactions.class);
    private TransDetailsUI transDetails = null;
    private final int KCC_ACC_NUM = 1;
    private final int REL_NUM = 2;
    private int ViewType;

    /**
     * Creates new form ViewAll
     */
    public ReleaseEnquiryUI() {
        setupInit();
        setupScreen();
        currDt = ClientUtil.getCurrentDate();
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        setCombos();
        transDetails = new TransDetailsUI(panMultiSearch);
        transDetails.setRefreshActDetails(true);
        panMultiSearch.setVisible(true);
        btnClear.setVisible(true);
        observable.resetForm();
        update(observable, null);
        txtAccNo.setAllowAll(true);
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
            observable = ReleaseEnquiryOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOBFields() {
        observable.setCboKCCProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboKCCProdId.getModel()).getKeyForSelected()));
        observable.setTxtAccNo(txtAccNo.getText());
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        observable.setTxtRelNo(txtRelNo.getText());
    }

    private void setCombos() {
        cboKCCProdId.setModel(observable.getCbmKccProdId());
    }

    private void addComboValues(List lst, CComboBox cbo) {
        HashMap hash = new HashMap();
        ArrayList arr = new ArrayList();
        String str = "";
        arr.add("");
        for (int i = 0; i < lst.size(); i++) {
            hash = (HashMap) lst.get(i);
            arr.add(hash.get("HEAD"));
        }
        EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(arr);
        cbo.setModel(cboModel);
        hash = null;
        arr = null;
        cboModel = null;
    }

    public void populateData() {
        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (observable.getTxtRelNo() != null) {
            if (observable.btnOtherDetPressed) {
                whereMap.put("RELEASE_NO", txtRelNo.getText());
                viewMap.put(CommonConstants.MAP_NAME, "getOtherDetailsForRelEnquiry");
            } else if (observable.btnTransPressed == true) {
                whereMap.put("RELEASE_NO", txtRelNo.getText());
                viewMap.put(CommonConstants.MAP_NAME, "getTransDetailsForRelEnquiry");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateData(viewMap, tblData);
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        } else {
            ClientUtil.displayAlert("Select Release Number!!!");
        }
        viewMap = null;
        whereMap = null;
    }

    public void show() {
        super.show();
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        this.dispose();

    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblKCCProdId = new com.see.truetransact.uicomponent.CLabel();
        cboKCCProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        panFind = new com.see.truetransact.uicomponent.CPanel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        btnOtherDet = new com.see.truetransact.uicomponent.CButton();
        lblReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo1 = new com.see.truetransact.uicomponent.CPanel();
        txtRelNo = new com.see.truetransact.uicomponent.CTextField();
        btnRelNo = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(860, 600));
        setPreferredSize(new java.awt.Dimension(860, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 280));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 280));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(450, 278));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(450, 278));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        lblKCCProdId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblKCCProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblKCCProdId, gridBagConstraints);

        cboKCCProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        cboKCCProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboKCCProdId.setPopupWidth(250);
        cboKCCProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKCCProdIdActionPerformed(evt);
            }
        });
        cboKCCProdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboKCCProdIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(cboKCCProdId, gridBagConstraints);

        lblAccNo.setText("Account No");
        lblAccNo.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblAccNo, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        panAcctNo.add(txtAccNo, new java.awt.GridBagConstraints());

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account No.");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAcctNo.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panAcctNo, gridBagConstraints);

        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAccNameValue.setText("Account Name");
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblAccNameValue, gridBagConstraints);

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(tdtToDate, gridBagConstraints);

        panFind.setBorder(javax.swing.BorderFactory.createTitledBorder("Press to view"));
        panFind.setMinimumSize(new java.awt.Dimension(220, 90));
        panFind.setPreferredSize(new java.awt.Dimension(220, 90));
        panFind.setLayout(new java.awt.GridBagLayout());

        btnView.setText("Transaction Details");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        panFind.add(btnView, new java.awt.GridBagConstraints());

        btnOtherDet.setText("Other Details");
        btnOtherDet.setMaximumSize(new java.awt.Dimension(145, 27));
        btnOtherDet.setMinimumSize(new java.awt.Dimension(145, 27));
        btnOtherDet.setPreferredSize(new java.awt.Dimension(145, 27));
        btnOtherDet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherDetActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        panFind.add(btnOtherDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 6, 3);
        panSearchCondition.add(panFind, gridBagConstraints);

        lblReleaseNo.setText("Release No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(lblReleaseNo, gridBagConstraints);

        panAcctNo1.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo1.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo1.setLayout(new java.awt.GridBagLayout());

        txtRelNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRelNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRelNoActionPerformed(evt);
            }
        });
        txtRelNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRelNoFocusLost(evt);
            }
        });
        panAcctNo1.add(txtRelNo, new java.awt.GridBagConstraints());

        btnRelNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRelNo.setToolTipText("Account No.");
        btnRelNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRelNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRelNoActionPerformed(evt);
            }
        });
        panAcctNo1.add(btnRelNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(panAcctNo1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14)); // NOI18N
        srcTable.setMinimumSize(new java.awt.Dimension(24, 40));
        srcTable.setOpaque(false);
        srcTable.setPreferredSize(new java.awt.Dimension(404, 404));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11"
            }
        ));
        tblData.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13)); // NOI18N
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setMinimumSize(new java.awt.Dimension(750, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(750, 40));
        panSearch.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOtherDetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherDetActionPerformed
        // TODO add your handling code here:   
        if (cboKCCProdId.getSelectedIndex() <= 0) {
            ClientUtil.showAlertWindow("Select Product Id!!!");
            return;
        }
        if (txtAccNo.getText().length() <= 0) {
            ClientUtil.showAlertWindow("Select Account Number!!!");
            return;
        }
        if (tdtFromDate.getDateValue().length() <= 0) {
            ClientUtil.showAlertWindow("Select From Date!!!");
            return;
        }
        if (txtRelNo.getText().length() > 0) {
            observable.btnOtherDetPressed = true;
            populateData();
            observable.btnOtherDetPressed = false;
        } else {
            ClientUtil.showAlertWindow("Select Release Number!!!");
        }
    }//GEN-LAST:event_btnOtherDetActionPerformed

    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);
        if (ViewType == KCC_ACC_NUM) {
            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            txtAccNoFocusLost(null);
        }
        if (ViewType == REL_NUM) {
            txtRelNo.setText(CommonUtil.convertObjToStr(hash.get("RELEASE_NO")));
        }
    }

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        if (cboKCCProdId.getSelectedIndex() > 0) {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            ViewType = KCC_ACC_NUM;
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
            whereMap.put(PROD_ID, observable.getCbmKccProdId().getKeyForSelected());
            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, "KCC A/c", viewMap).show();
        } else {
            ClientUtil.showAlertWindow("Product Id should not be empty!!!");
            return;
        }
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:               
        observable.resetAccno();
        if (cboKCCProdId.getSelectedIndex() > 0) {
            if (txtAccNo.getText().length() > 0) {
                transDetails.setTransDetails(null, null, null);
                if (txtAccNo.getText().length() > 0) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", txtAccNo.getText());
                    List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", whereMap);
                    if (mapDataList != null && mapDataList.size() > 0) {
                        whereMap = (HashMap) mapDataList.get(0);
                        txtAccNo.setText(CommonUtil.convertObjToStr(whereMap.get("ACT_NUM")));
                    } else {
                        ClientUtil.displayAlert("Invalid Account No.");
                        lblAccNameValue.setText("");
                        txtAccNo.setText("");
                        return;
                    }
                }
                if (observable.getCboKCCProdId().length() > 0) {
                    observable.setTxtAccNo(txtAccNo.getText());
                    transDetails.setTransDetails("AD", ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
                    observable.setAccountName();
                    lblAccNameValue.setText(observable.getLblAccNameValue());
                } else {
                    ClientUtil.displayAlert("Enter Product Id");
                    btnClearActionPerformed(null);
                    txtAccNo.setText("");
                    return;
                }
            }
        } else {
            ClientUtil.displayAlert("Enter Product Id");
            btnClearActionPerformed(null);
            txtAccNo.setText("");
            return;
        }
    }//GEN-LAST:event_txtAccNoFocusLost

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        txtRelNo.setText("");
        if (tdtToDate.getDateValue().length() > 0) {
            observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
            java.util.Date from = observable.getTdtFromDate();
            java.util.Date to = observable.getTdtToDate();
            if (from != null && to != null && DateUtil.dateDiff(from, to) < 0) {
                displayAlert("To date should be greater than From Date...");
                tdtToDate.setDateValue("");
                tdtToDate.requestFocus();
                return;
            }
            if (DateUtil.dateDiff(to, currDt) < 0) {
                ClientUtil.showAlertWindow("To Date Should Be Equal Or less than current Date !!!");
                tdtToDate.setDateValue("");
                return;
            }
        }
    }//GEN-LAST:event_tdtToDateFocusLost

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        txtRelNo.setText("");
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void cboKCCProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKCCProdIdActionPerformed
        // TODO add your handling code here:
        System.out.println("#$#$ prodId : " + ((ComboBoxModel) cboKCCProdId.getModel()).getKeyForSelected());
        observable.setCboKCCProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboKCCProdId.getModel()).getKeyForSelected()));
    }//GEN-LAST:event_cboKCCProdIdActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        if (cboKCCProdId.getSelectedIndex() <= 0) {
            ClientUtil.showAlertWindow("Select Product Id!!!");
            return;
        }
        if (txtAccNo.getText().length() <= 0) {
            ClientUtil.showAlertWindow("Select Account Number!!!");
            return;
        }
        if (tdtFromDate.getDateValue().length() <= 0) {
            ClientUtil.showAlertWindow("Select From Date!!!");
            return;
        }
        if (txtRelNo.getText().length() > 0) {
            observable.btnTransPressed = true;
            populateData();
            observable.btnTransPressed = false;
        } else {
            ClientUtil.showAlertWindow("Select Release Number!!!");
            return;
        }
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        btnClearActionPerformed(null);
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        observable.resetForm();
        update(observable, null);
        behavesLike = "";
        transDetails.setTransDetails(null, null, null);
        lblAccNameValue.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtRelNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRelNoActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtRelNoActionPerformed

    private void txtRelNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRelNoFocusLost
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtRelNoFocusLost

    private void btnRelNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRelNoActionPerformed
        // TODO add your handling code here:
        observable.resetRelNo();        
        if (cboKCCProdId.getSelectedIndex() <= 0) {
            ClientUtil.showAlertWindow("Select Product Id!!!");
            return;
        }
        if (txtAccNo.getText().length() <= 0) {
            ClientUtil.showAlertWindow("Select Account Number!!!");
            return;
        }
        if (tdtFromDate.getDateValue().length() > 0) {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getReleaseNumberForRelEnquiry");            
             whereMap.put("FROM_DATE",setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));             
            if (tdtToDate.getDateValue().length() > 0) {
                whereMap.put("TO_DATE", setProperDtFormat(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
            } else {
                whereMap.put("TO_DATE", currDt);
            }
            ViewType = REL_NUM;
            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, viewMap).show();
        } else {
            ClientUtil.showAlertWindow("Select From Date!!!");
            return;
        }
    }//GEN-LAST:event_btnRelNoActionPerformed

    private void cboKCCProdIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboKCCProdIdFocusLost
        // TODO add your handling code here:
        observable.resetProdId();
        resetProdId();
        transDetails.setTransDetails(null, null, null);
    }//GEN-LAST:event_cboKCCProdIdFocusLost

    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }
    
    
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)currDt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    

    private void internationalize() {
//            lblSearch.setText(resourceBundle.getString("lblSearch"));
//            btnSearch.setText(resourceBundle.getString("btnSearch"));
//            chkCase.setText(resourceBundle.getString("chkCase"));
//            btnOk.setText(resourceBundle.getString("btnOk"));
//            btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    public void resetProdId() {
        txtAccNo.setText("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        txtRelNo.setText("");
        lblAccNameValue.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();

        HashMap where = new HashMap();
        where.put("beh", "CA");

        mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
        new ViewAll(mapParam).show();
    }

    public void update(Observable observed, Object arg) {
        ((ComboBoxModel) cboKCCProdId.getModel()).setKeyForSelected(observable.getCboKCCProdId());
        txtAccNo.setText(observable.getTxtAccNo());
        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
        tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
        txtRelNo.setText(observable.getTxtRelNo());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnOtherDet;
    private com.see.truetransact.uicomponent.CButton btnRelNo;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboKCCProdId;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblKCCProdId;
    private com.see.truetransact.uicomponent.CLabel lblReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAcctNo1;
    private com.see.truetransact.uicomponent.CPanel panFind;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtRelNo;
    // End of variables declaration//GEN-END:variables
}

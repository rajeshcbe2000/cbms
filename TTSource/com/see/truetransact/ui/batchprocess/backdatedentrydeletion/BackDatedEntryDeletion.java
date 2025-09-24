/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BackDatedEntryDeletion.java
 *
 * Created on August 24, 2003, 1:46 PM
 */

package com.see.truetransact.ui.batchprocess.backdatedentrydeletion;

import com.see.truetransact.ui.common.viewall.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

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
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;
import com.see.truetransact.ui.transaction.transfer.TransferUI;
import com.see.truetransact.ui.transaction.transfer.TransferOB;
import java.util.List;
import com.see.truetransact.ui.transaction.clearing.InwardClearingUI;
import com.see.truetransact.ui.transaction.clearing.outward.OutwardClearingUI;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;

/**
 * @author  balachandar
 */
public class BackDatedEntryDeletion extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private final ViewAllRB resourceBundle = new ViewAllRB();
    private BackDatedEntryDeletionOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    CPanel panelParent = null;
    int amtColumnNo=0;
    double tot = 0;
    String amtColName = "";
    Date currDt = null;
    
    private final static Logger log = Logger.getLogger(BackDatedEntryDeletion.class);
    
    /** Creates new form ViewAll */
    public BackDatedEntryDeletion() {
        currDt = ClientUtil.getCurrentDate();
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        toFront();
        setCombos();
        panMultiSearch.setVisible(true);
        if (parent != null) {
            parent.toFront();
            setTitle("List for " + parent.getTitle());
        }
       panSearchCriteria.setVisible(false);
    }
    
    private void setupScreen() {
        btnReprint.setEnabled(false);
        cboBranchID.setEnabled(false);
        cboTransMode.setEnabled(false);
        cboDate.setEnabled(false);
        
        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
    
    private void setObservable() {
        try {
            observable = new BackDatedEntryDeletionOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setCombos() {
        HashMap where = new HashMap();
        List lst = null;
        cboProdType.setModel(observable.getMainProductTypeModel());
        cboTransMode.addItem("");
        cboTransMode.addItem("TRANSFER");
        cboTransMode.setSelectedItem("TRANSFER");
        lst = ClientUtil.executeQuery("getAcHdId", where);
        addComboValues(lst, cboAcHdID);
        lst = ClientUtil.executeQuery("getBranchID", where);
        addComboValues(lst, cboBranchID);
        cboBranchID.setSelectedItem(ProxyParameters.BRANCH_ID);
        cboStatus.addItem("");
        cboStatus.addItem("CREATED");
        cboStatus.addItem("MODIFIED");
        cboStatus.setSelectedItem("CREATED");
        cboTransType.addItem("");
        cboTransType.addItem("DEBIT");
        cboTransType.addItem("CREDIT");
        cboDate.addItem("");
        cboDate.addItem("TRANS_DT");
        cboDate.setSelectedItem("TRANS_DT");
        tdtDate.setDateValue(DateUtil.getStringDate(currDt));
        lst = null;
        where = null;
    }
    
    private void addComboValues(List lst, CComboBox cbo) {
        HashMap hash = new HashMap();
        ArrayList arr = new ArrayList();
        String str ="";
        arr.add("");
        for(int i=0; i<lst.size(); i++) {
            hash = (HashMap)lst.get(i);
            arr.add(hash.get("HEAD"));
        }
        EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(arr);
        cbo.setModel(cboModel);
        hash = null;
        arr = null;
        cboModel = null;
    }
    
    public void populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getAllBackDatedTransactionsToList");
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("TRANS_MODE", cboTransMode.getSelectedItem().toString());
        whereMap.put("AC_HD_ID", cboAcHdID.getSelectedItem().toString());
        whereMap.put("BRANCH_ID", cboBranchID.getSelectedItem().toString());
        whereMap.put("PROD_TYPE", ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
        if(cboStatus.getSelectedItem().toString()!= null && !cboStatus.getSelectedItem().toString().equals("")){
            whereMap.put("STATUS", cboStatus.getSelectedItem().toString());
        }else{
            whereMap.put("STATUSALL", "STATUSALL");
        }
        whereMap.put("TRANS_TYPE", cboTransType.getSelectedItem().toString());
        if(chkUnAuth.isSelected())
        whereMap.put("UNAUTHORIZED","UNAUTHORIZED");  
        int i = cboDate.getSelectedIndex();
        switch (i) {
            case 1 :
                whereMap.put("TRANS_FROM_DT", tdtDate.getDateValue());
                whereMap.put("TRANS_TO_DT", tdtToDate.getDateValue());
                break;
            case 2 :
                whereMap.put("INST_FROM_DT", tdtDate.getDateValue());
                whereMap.put("INST_TO_DT", tdtToDate.getDateValue());
                break;
            case 3 :
                whereMap.put("AUTH_FROM_DT", tdtDate.getDateValue());
                whereMap.put("AUTH_TO_DT", tdtToDate.getDateValue());
                break;
            case 4 :
                whereMap.put("STATUS_FROM_DT", tdtDate.getDateValue());
                whereMap.put("STATUS_TO_DT", tdtToDate.getDateValue());
                break;
        }
//        System.out.println("#### where map : "+whereMap);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            ArrayList headCopy = new ArrayList();
            if (heading!=null) {
                headCopy.addAll(heading);
            }
            if (headCopy.size()>0) {
                headCopy.remove("TRANS_DT");
                headCopy.remove("INST_DT");
                headCopy.remove("AUTHORIZE_DT");
                headCopy.remove("STATUS_DT");
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(headCopy);
                cboSearchCol.setModel(cboModel);
                panSearchCriteria.setVisible(true);
                if (tblData.getRowCount()>0) {
                    calculateTot();
                }
            } else {
                panSearchCriteria.setVisible(false);
            }
            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void show() {
        if (observable.isAvailable()) {
            super.show();
        }
    }
    
//    public void setVisible(boolean visible) {
//        if (observable.isAvailable()) {
//            super.setVisible(visible);
//        }
//    }
    
    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        this.dispose();        

    }
    
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboStatus = new com.see.truetransact.uicomponent.CComboBox();
        cboTransType = new com.see.truetransact.uicomponent.CComboBox();
        lblTransMode = new com.see.truetransact.uicomponent.CLabel();
        lblAcHdID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchID = new com.see.truetransact.uicomponent.CLabel();
        cboTransMode = new com.see.truetransact.uicomponent.CComboBox();
        cboAcHdID = new com.see.truetransact.uicomponent.CComboBox();
        cboBranchID = new com.see.truetransact.uicomponent.CComboBox();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cboDate = new com.see.truetransact.uicomponent.CComboBox();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        panFind = new com.see.truetransact.uicomponent.CPanel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        chkUnAuth = new com.see.truetransact.uicomponent.CCheckBox();
        lblUnAuth = new com.see.truetransact.uicomponent.CLabel();
        btnReprint = new com.see.truetransact.uicomponent.CButton();
        panSearchCriteria = new com.see.truetransact.uicomponent.CPanel();
        lblSearch = new com.see.truetransact.uicomponent.CLabel();
        cboSearchCol = new com.see.truetransact.uicomponent.CComboBox();
        cboSearchCriteria = new com.see.truetransact.uicomponent.CComboBox();
        txtSearchData = new javax.swing.JTextField();
        chkCase = new com.see.truetransact.uicomponent.CCheckBox();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSelTrans = new com.see.truetransact.uicomponent.CLabel();
        lblAllTrans = new com.see.truetransact.uicomponent.CLabel();
        lblSelTransValue = new com.see.truetransact.uicomponent.CLabel();
        lblAllTransValue = new com.see.truetransact.uicomponent.CLabel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(850, 675));
        setPreferredSize(new java.awt.Dimension(850, 675));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(700, 90));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(700, 90));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblProductType, gridBagConstraints);

        lblStatus.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblStatus, gridBagConstraints);

        lblTransType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblTransType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboProdType, gridBagConstraints);

        cboStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboStatus, gridBagConstraints);

        cboTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboTransType, gridBagConstraints);

        lblTransMode.setText("Transaction Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblTransMode, gridBagConstraints);

        lblAcHdID.setText("Account Head ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblAcHdID, gridBagConstraints);

        lblBranchID.setText("Branch ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblBranchID, gridBagConstraints);

        cboTransMode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboTransMode, gridBagConstraints);

        cboAcHdID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboAcHdID, gridBagConstraints);

        cboBranchID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboBranchID, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(180, 80));
        cPanel1.setPreferredSize(new java.awt.Dimension(180, 80));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        cboDate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(cboDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tdtDate, gridBagConstraints);

        tdtToDate.setMinimumSize(new java.awt.Dimension(101, 0));
        tdtToDate.setPreferredSize(new java.awt.Dimension(101, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tdtToDate, gridBagConstraints);

        lblTransDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblTransDate, gridBagConstraints);

        lblToDate.setText("To Date");
        lblToDate.setMaximumSize(new java.awt.Dimension(45, 0));
        lblToDate.setMinimumSize(new java.awt.Dimension(45, 0));
        lblToDate.setPreferredSize(new java.awt.Dimension(45, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblToDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cPanel1, gridBagConstraints);

        panFind.setMinimumSize(new java.awt.Dimension(100, 100));
        panFind.setPreferredSize(new java.awt.Dimension(100, 100));
        panFind.setLayout(new java.awt.GridBagLayout());

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panFind.add(btnView, gridBagConstraints);

        chkUnAuth.setMaximumSize(new java.awt.Dimension(21, 0));
        chkUnAuth.setMinimumSize(new java.awt.Dimension(21, 0));
        chkUnAuth.setPreferredSize(new java.awt.Dimension(21, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFind.add(chkUnAuth, gridBagConstraints);

        lblUnAuth.setText("UnAuthorized");
        lblUnAuth.setMaximumSize(new java.awt.Dimension(78, 0));
        lblUnAuth.setMinimumSize(new java.awt.Dimension(80, 0));
        lblUnAuth.setPreferredSize(new java.awt.Dimension(80, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFind.add(lblUnAuth, gridBagConstraints);

        btnReprint.setText("Reprint");
        btnReprint.setMaximumSize(new java.awt.Dimension(75, 0));
        btnReprint.setMinimumSize(new java.awt.Dimension(75, 0));
        btnReprint.setPreferredSize(new java.awt.Dimension(75, 0));
        btnReprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panFind.add(btnReprint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(panFind, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panSearchCriteria.setMinimumSize(new java.awt.Dimension(600, 35));
        panSearchCriteria.setPreferredSize(new java.awt.Dimension(600, 35));
        panSearchCriteria.setLayout(new java.awt.GridBagLayout());

        lblSearch.setText("Search");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSearchCriteria.add(lblSearch, gridBagConstraints);

        cboSearchCol.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSearchCriteria.add(cboSearchCol, gridBagConstraints);

        cboSearchCriteria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Starts with", "Ends with", "Exact Match", "Pattern Match" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSearchCriteria.add(cboSearchCriteria, gridBagConstraints);

        txtSearchData.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSearchCriteria.add(txtSearchData, gridBagConstraints);

        chkCase.setText("Match Case");
        panSearchCriteria.add(chkCase, new java.awt.GridBagConstraints());

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnSearch.setText("Find");
        btnSearch.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnSearch.setMaximumSize(new java.awt.Dimension(65, 30));
        btnSearch.setMinimumSize(new java.awt.Dimension(65, 27));
        btnSearch.setPreferredSize(new java.awt.Dimension(65, 27));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        panSearchCriteria.add(btnSearch, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panSearchCondition.add(panSearchCriteria, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

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
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblDataMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
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

        panSearch.setMinimumSize(new java.awt.Dimension(823, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(823, 40));
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

        lblSelTrans.setText("Selected Transactions Total ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panSearch.add(lblSelTrans, gridBagConstraints);

        lblAllTrans.setText("All Transactions Total ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panSearch.add(lblAllTrans, gridBagConstraints);

        lblSelTransValue.setText("0");
        lblSelTransValue.setMaximumSize(new java.awt.Dimension(200, 15));
        lblSelTransValue.setMinimumSize(new java.awt.Dimension(200, 15));
        lblSelTransValue.setPreferredSize(new java.awt.Dimension(200, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panSearch.add(lblSelTransValue, gridBagConstraints);

        lblAllTransValue.setText("0");
        lblAllTransValue.setMaximumSize(new java.awt.Dimension(200, 15));
        lblAllTransValue.setMinimumSize(new java.awt.Dimension(200, 15));
        lblAllTransValue.setPreferredSize(new java.awt.Dimension(200, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panSearch.add(lblAllTransValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sptLine, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblDataMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseDragged
        // TODO add your handling code here:
//        Point p = evt.getPoint();
//        tot = tot + Double.parseDouble(
//        tblData.getModel().getValueAt(
//        tblData.rowAtPoint(p),
//        tblData.columnAtPoint(p)).toString());
//        lblSelTransValue.setText(String.valueOf(tot));            
    }//GEN-LAST:event_tblDataMouseDragged

    private void cboDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDateActionPerformed
        // TODO add your handling code here:
        if (cboDate.getSelectedIndex()==0) {
            tdtDate.setDateValue("");
            tdtToDate.setDateValue("");
        }
    }//GEN-LAST:event_cboDateActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        populateData();
        btnReprint.setEnabled(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // Add your handling code here:
        String searchTxt = txtSearchData.getText().trim();
        if (searchTxt.length()<=0) {
            if (observable.getDataSize()>=observable.MAXDATA) {
                int opt=ClientUtil.confirmationAlert("You have not entered the search String.\nThis will take several minutes.\nContinue Anyway?");
                if (opt!=0) {
                    txtSearchData.requestFocus();
                    return;
                }
                else {
                    observable.populateTable();
                    calculateTot();
                    return;
                }
            }
        }
        if (!chkCase.isSelected()) searchTxt = searchTxt.toUpperCase();
        
        //tblData.setModel(observable.getTableModel());
        //tblData.revalidate();
        //                observable.refreshTable();
        
        int selCol = cboSearchCol.getSelectedIndex();
        int selColCri = cboSearchCriteria.getSelectedIndex();
        
        observable.searchData(searchTxt, selCol, selColCri, chkCase.isSelected());
    }//GEN-LAST:event_btnSearchActionPerformed

    public void calculateTot() {
//        int colcnt = tblData.getColumnCount();
//        for (int c=0; c<colcnt; c++) {
//            amtColName = tblData.getColumnName(c);
//            if (amtColName.equals("AMOUNT")) {
//                amtColPos = c;
//                break;
//            }
//        }
        
        int rowcnt = tblData.getRowCount();
        int colcnt = tblData.getColumnCount();
//        double tot = 0;
//        System.out.println("#### rows : "+rows);
        System.out.println("#### rowcnt : "+rowcnt);
        String colName="";
        for (int i=0; i<colcnt; i++) {
            colName = tblData.getColumnName(i);
            if (colName.equals("AMOUNT")) {
                amtColumnNo = i;
                break;
            }
        }
        tot = 0;
        for (int i=0; i<rowcnt; i++) {
            tot = tot + CommonUtil.convertObjToDouble(tblData.getValueAt(i, amtColumnNo).toString()).doubleValue();
//            System.out.println("#### tot : "+tot);
            lblAllTransValue.setText(CurrencyValidation.formatCrore(String.valueOf(tot)));
        }        
    }
    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
        //                    if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
        //                        whenTableRowSelected();
        //                    }
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
            System.out.println("inside here now");
            String trans_id = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 0));
//            String dt= CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 5));
            String bran = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 20));
            String act_num = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 3));
            String trans_type = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 19));
            Date dt = (Date) tblData.getValueAt(tblData.getSelectedRow(), 5);
            System.out.println("trans_id" + trans_id + "bran" + bran + "act_num" + act_num + "trans_type" + trans_type + "dt" + dt);
            HashMap map = new HashMap();
            map.put("TRANS_ID", trans_id);
            map.put("ACCOUNT NO", act_num);
            map.put("TRANS_DT", dt);
            if (trans_type.equals("CASH")) {
                CashTransactionUI CashTransaction = new CashTransactionUI();
                com.see.truetransact.ui.TrueTransactMain.showScreen(CashTransaction);
                CashTransaction.setTitle("Cash" + "[" + bran + "]");
                CashTransaction.setSelectedBranchID(bran);
                CashTransaction.setViewType(5);
                CashTransaction.fillData(map);
                CashTransaction.btnCheck();
            } else if (trans_type.equals("TRANSFER")) {
                TransferUI transfer = new TransferUI();
                TransferOB transferOB = new TransferOB();
                com.see.truetransact.ui.TrueTransactMain.showScreen(transfer);
                transfer.setTitle("Transfer" + "[" + bran + "]");
                String batch_id = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 1));
                transfer.setBatchIdForEdit(batch_id);
                transfer.setTransactionDateForEdit(dt);
                transfer.setBackDatedTransDate(dt);
                transfer.setTransactionInitBranForEdit(bran);
                transfer.populateUIData(17);
                transfer.btnCheck();
            } else if (trans_type.equals("CLEARING")) {
                map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                map.put("TRANS_DT", currDt);
                List lst = ClientUtil.executeQuery("OutwardOrInward", map);
                if (lst != null && lst.size() > 0) {
                    HashMap whereMap = new HashMap();
                    whereMap = (HashMap) lst.get(0);
                    if (whereMap.get("TABLE_NAME").equals("INWARD")) {
                        InwardClearingUI Inward = new InwardClearingUI(bran);
                        com.see.truetransact.ui.TrueTransactMain.showScreen(Inward);
                        Inward.setTitle("Inward Clearing" + "[" + bran + "]");
                        Inward.setSelectedBranchID(bran);
                        map.put("INWARD ID", trans_id);
                        map.put("INITIATED_BRANCH", bran);
                        Inward.setViewType(3);
                        Inward.fillData(map);
                        Inward.btnCheck();
                    } else if (whereMap.get("TABLE_NAME").equals("OUTWARD")) {
                        OutwardClearingUI Outward = new OutwardClearingUI();
                        com.see.truetransact.ui.TrueTransactMain.showScreen(Outward);
                        Outward.setTitle("Outward Clearing" + "[" + bran + "]");
                        Outward.setSelectedBranchID(bran);
                        map.put("BATCHID", trans_id);
                        Outward.setViewType("Enquirystatus");
                        Outward.fillData(map);
                        Outward.btnCheck();
                    }
                }
            }
        }
        int colcnt = tblData.getColumnCount();
        for (int c = 0; c < colcnt; c++) {
            amtColName = tblData.getColumnName(c);
            if (amtColName.equals("AMOUNT")) {
                amtColumnNo = c;
                break;
            }
        }
        int[] rows = tblData.getSelectedRows();
        int rowcnt = tblData.getSelectedRowCount();
        tot = 0;
        for (int i = 0; i < rowcnt; i++) {
            tot = tot + CommonUtil.convertObjToDouble(tblData.getValueAt(rows[i], amtColumnNo).toString()).doubleValue();
            System.out.println("#### tot : " + tot);
            lblSelTransValue.setText(CurrencyValidation.formatCrore(String.valueOf(tot)));
        }
    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // Add your handling code here:
        Point p = evt.getPoint();
        String tip =
        String.valueOf(
        tblData.getModel().getValueAt(
        tblData.rowAtPoint(p),
        tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);
//        System.out.println("#### tblData.getSelectedColumn() : "+tblData.columnAtPoint(p));
//        System.out.println("#### amtColumnNo : "+amtColumnNo);
        if (tblData.columnAtPoint(p) == amtColumnNo) {
            tblData.setToolTipText("Press Shift and select amount to get selective total");
        }
    }//GEN-LAST:event_tblDataMouseMoved

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintActionPerformed
        // TODO add your handling code here:
        if(tblData.getSelectedRow()==-1){
            ClientUtil.showAlertWindow("Please select a row in the table grid");
            return;
        }
        int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                 TTIntegration ttIntgration = null;
                HashMap printParamMap = new HashMap();
                printParamMap.put("TransDt", tblData.getValueAt(tblData.getSelectedRow(), 5));
                printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
//                printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 0));
//                ttIntgration.setParam(printParamMap);
                    if (tblData.getValueAt(tblData.getSelectedRow(), 1) != null) {
                        //printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 1));
                         printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 23));
                        ttIntgration.setParam(printParamMap);
                        ttIntgration.integrationForPrint("ReceiptPayment");
                    } else if (tblData.getValueAt(tblData.getSelectedRow(), 6).equals("DEBIT")) {
                       // printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 0));
                        printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 23));
                        ttIntgration.setParam(printParamMap);
                        ttIntgration.integrationForPrint("CashPayment", false);
                    } else {
                       // printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 0));
                        printParamMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 23));
                        ttIntgration.setParam(printParamMap);
                        ttIntgration.integrationForPrint("CashReceipt", false);
                    }
            }
    }//GEN-LAST:event_btnReprintActionPerformed
    
        private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }                            
        
        private void internationalize() {
            lblSearch.setText(resourceBundle.getString("lblSearch"));
            btnSearch.setText(resourceBundle.getString("btnSearch"));
            chkCase.setText(resourceBundle.getString("chkCase"));
            btnClose.setText("Close");
        }
        
        /**
         * @param args the command line arguments
         */
        public static void main(String args[]) {
            HashMap mapParam = new HashMap();

            HashMap where = new HashMap();
            where.put("beh", "CA");

            mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
            //mapParam.put("WHERE", where);
            // HashMap rMap = (HashMap) dao.getData(mapParam);

            // HashMap testMap = new HashMap();
            //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
            new ViewAll(mapParam).show();
        }

        public void update(Observable o, Object arg) {

        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnReprint;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboAcHdID;
    private com.see.truetransact.uicomponent.CComboBox cboBranchID;
    private com.see.truetransact.uicomponent.CComboBox cboDate;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCol;
    private com.see.truetransact.uicomponent.CComboBox cboSearchCriteria;
    private com.see.truetransact.uicomponent.CComboBox cboStatus;
    private com.see.truetransact.uicomponent.CComboBox cboTransMode;
    private com.see.truetransact.uicomponent.CComboBox cboTransType;
    private com.see.truetransact.uicomponent.CCheckBox chkCase;
    private com.see.truetransact.uicomponent.CCheckBox chkUnAuth;
    private com.see.truetransact.uicomponent.CLabel lblAcHdID;
    private com.see.truetransact.uicomponent.CLabel lblAllTrans;
    private com.see.truetransact.uicomponent.CLabel lblAllTransValue;
    private com.see.truetransact.uicomponent.CLabel lblBranchID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CLabel lblSelTrans;
    private com.see.truetransact.uicomponent.CLabel lblSelTransValue;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CLabel lblTransMode;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
    private com.see.truetransact.uicomponent.CLabel lblUnAuth;
    private com.see.truetransact.uicomponent.CPanel panFind;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panSearchCriteria;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private javax.swing.JTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}


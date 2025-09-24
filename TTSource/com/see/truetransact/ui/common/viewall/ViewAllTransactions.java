/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewAllTransactions.java
 *
 * Created on August 24, 2003, 1:46 PM
 */
package com.see.truetransact.ui.common.viewall;

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
import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 * @author balachandar
 */
public class ViewAllTransactions extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {

    private final ViewAllRB resourceBundle = new ViewAllRB();
    private ViewAllTransactionsOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    CPanel panelParent = null;
    int amtColumnNo = 0;
    double tot = 0;
    String amtColName = "";
    Date currDt = null;
    ArrayList colorList = new ArrayList();
    private final static Logger log = Logger.getLogger(ViewAllTransactions.class);
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    NewAuthorizeListUI newauthorizeListUI = null;

    /**
     * Creates new form ViewAll
     */
    public ViewAllTransactions() {
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
//        populateData();
        panMultiSearch.setVisible(true);
        if (parent != null) {
            parent.toFront();
            setTitle("List for " + parent.getTitle());
        }
        panSearchCriteria.setVisible(false);
        enableIntBranchButon(false);
    }

    private void setupScreen() {
        btnReprint.setEnabled(false);
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
        //added by anju laksmi 15/10/2014
        lblAllTrans.setVisible(false);
        lblAllTransValue.setVisible(false);
    }

    private void setObservable() {
        try {
            observable = new ViewAllTransactionsOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCombos() {
        HashMap where = new HashMap();
        List lst = null;
//        lst = ClientUtil.executeQuery("getTransMode", where);
//        System.out.println("### List : "+lst);
        cboTransMode.addItem("");
        cboTransMode.addItem("CASH");
        cboTransMode.addItem("TRANSFER");
//        addComboValues(lst,cboTransMode);
        lst = ClientUtil.executeQuery("getAcHdId", where);
//        System.out.println("### List : "+lst);
        addComboValues(lst, cboAcHdID);
        lst = ClientUtil.executeQuery("getBranchID", where);
//        System.out.println("### List : "+lst);
        addComboValues(lst, cboBranchID);
        cboBranchID.setSelectedItem(ProxyParameters.BRANCH_ID);
        lst = ClientUtil.executeQuery("getProdID", where);
//        System.out.println("### List : "+lst);
        addComboValues(lst, cboProdId);
//        lst = ClientUtil.executeQuery("getStatus", where);
        cboStatus.addItem("");
        cboStatus.addItem("CREATED");
        cboStatus.addItem("DELETED");
        cboStatus.addItem("MODIFIED");
        cboStatus.setSelectedItem("CREATED");
//        System.out.println("### List : "+lst);
//        addComboValues(lst,cboStatus);
//        lst = ClientUtil.executeQuery("getTransType", where);
//        System.out.println("### List : "+lst);
        cboTransType.addItem("");
        cboTransType.addItem("DEBIT");
        cboTransType.addItem("CREDIT");
//        addComboValues(lst,cboTransType);
        cboDate.addItem("");
        cboDate.addItem("TRANS_DT");
        cboDate.addItem("INST_DT");
        cboDate.addItem("AUTHORIZE_DT");
        cboDate.addItem("STATUS_DT");
        cboDate.setSelectedItem("TRANS_DT");
        tdtFromDate.setDateValue(DateUtil.getStringDate(currDt)); // Commented by nithya on 23-09-2016 // Removed the comment on 24-11-2016  
        lst = null;
        where = null;
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
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getAllTransactionsToList");
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("TRANS_MODE", cboTransMode.getSelectedItem().toString());
        whereMap.put("AC_HD_ID", cboAcHdID.getSelectedItem().toString());
        whereMap.put("BRANCH_ID", cboBranchID.getSelectedItem().toString());
        whereMap.put("PROD_ID", cboProdId.getSelectedItem().toString());
        whereMap.put("STATUS", cboStatus.getSelectedItem().toString());
        whereMap.put("TRANS_TYPE", cboTransType.getSelectedItem().toString());
        if (chkUnAuth.isSelected()) {
            whereMap.put("UNAUTHORIZED", "UNAUTHORIZED");
        }
        int i = cboDate.getSelectedIndex();
        switch (i) {
            case 1:
                whereMap.put("TRANS_FROM_DT", tdtFromDate.getDateValue());
                whereMap.put("TRANS_TO_DT", tdtToDate.getDateValue());
                break;
            case 2:
                whereMap.put("INST_FROM_DT", tdtFromDate.getDateValue());
                whereMap.put("INST_TO_DT", tdtToDate.getDateValue());
                break;
            case 3:
                whereMap.put("AUTH_FROM_DT", tdtFromDate.getDateValue());
                whereMap.put("AUTH_TO_DT", tdtToDate.getDateValue());
                break;
            case 4:
                whereMap.put("STATUS_FROM_DT", tdtFromDate.getDateValue());
                whereMap.put("STATUS_TO_DT", tdtToDate.getDateValue());
                break;
        }
//        System.out.println("#### where map : "+whereMap);
        // Added by nithya on 27-08-2016 for 5050
        whereMap.put("NARRATION",txtNarration.getText());
        whereMap.put("PARTICULARS",txtParticulars.getText());
        // End
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            ArrayList headCopy = new ArrayList();
            if (heading != null) {
                headCopy.addAll(heading);
            }
            
            //Added By Kannan AR to set color for IBT transaction KDSA-718
            if (observable.getData() != null && observable.getData().size() > 0) {
                colorList = new ArrayList();
                for (int j = 0; j < observable.getData().size(); j++) {
                    setColour();
                }
            }
            System.out.println("print....");
            if (headCopy.size() > 0) {
                headCopy.remove("TRANS_DT");
                headCopy.remove("INST_DT");
                headCopy.remove("AUTHORIZE_DT");
                headCopy.remove("STATUS_DT");
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(headCopy);
                cboSearchCol.setModel(cboModel);
                panSearchCriteria.setVisible(true);
                if (tblData.getRowCount() > 0) {
                    calculateTot();
                }
            } else {
                panSearchCriteria.setVisible(false);
            }
            heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
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

    private void setColour() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String str = CommonUtil.convertObjToStr(table.getValueAt(row, 26));
                //if (colorList.contains(String.valueOf(row))) {
                if (str.equals("Y")) {
                    //setForeground(Color.magenta);
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblData.setDefaultRenderer(Object.class, renderer);
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
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
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
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        panFind = new com.see.truetransact.uicomponent.CPanel();
        btnView = new com.see.truetransact.uicomponent.CButton();
        chkUnAuth = new com.see.truetransact.uicomponent.CCheckBox();
        lblUnAuth = new com.see.truetransact.uicomponent.CLabel();
        btnReprint = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtNarration = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
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
        ViewInterBranch = new com.see.truetransact.uicomponent.CButton();
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
        panMultiSearch.setMinimumSize(new java.awt.Dimension(760, 110));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(760, 110));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(lblProductId, gridBagConstraints);

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

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(cboProdId, gridBagConstraints);

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
        cPanel1.add(tdtFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tdtToDate, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
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

        panFind.setMinimumSize(new java.awt.Dimension(130, 100));
        panFind.setPreferredSize(new java.awt.Dimension(130, 100));
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

        chkUnAuth.setMargin(new java.awt.Insets(3, 3, 3, 3));
        chkUnAuth.setMaximumSize(new java.awt.Dimension(25, 25));
        chkUnAuth.setMinimumSize(new java.awt.Dimension(25, 25));
        chkUnAuth.setPreferredSize(new java.awt.Dimension(25, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFind.add(chkUnAuth, gridBagConstraints);

        lblUnAuth.setText("UnAuthorized");
        lblUnAuth.setMinimumSize(new java.awt.Dimension(80, 18));
        lblUnAuth.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFind.add(lblUnAuth, gridBagConstraints);

        btnReprint.setText("Reprint");
        btnReprint.setMaximumSize(new java.awt.Dimension(100, 27));
        btnReprint.setMinimumSize(new java.awt.Dimension(100, 27));
        btnReprint.setPreferredSize(new java.awt.Dimension(100, 27));
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

        cLabel1.setText("Narration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panMultiSearch.add(cLabel1, gridBagConstraints);

        txtNarration.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 20);
        panMultiSearch.add(txtNarration, gridBagConstraints);

        cLabel2.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        panMultiSearch.add(cLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 161;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panMultiSearch.add(txtParticulars, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panSearchCriteria.setMinimumSize(new java.awt.Dimension(600, 45));
        panSearchCriteria.setPreferredSize(new java.awt.Dimension(600, 45));
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
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
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

        ViewInterBranch.setText("ViewInterBranch");
        ViewInterBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewInterBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panSearch.add(ViewInterBranch, gridBagConstraints);

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
        if (cboDate.getSelectedIndex() == 0) {
            tdtFromDate.setDateValue("");
            tdtToDate.setDateValue("");
        }
    }//GEN-LAST:event_cboDateActionPerformed

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException, Exception /**
             * Execute some operation
             */
            {
                populateData();
                btnReprint.setEnabled(true);
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
        } //Progress bar code ends here 
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // Add your handling code here:
        String searchTxt = txtSearchData.getText().trim();
        if (searchTxt.length() <= 0) {
            if (observable.getDataSize() >= observable.MAXDATA) {
                int opt = ClientUtil.confirmationAlert("You have not entered the search String.\nThis will take several minutes.\nContinue Anyway?");
                if (opt != 0) {
                    txtSearchData.requestFocus();
                    return;
                } else {
                    observable.populateTable();
                    calculateTot();
                    return;
                }
            }
        }
        if (!chkCase.isSelected()) {
            searchTxt = searchTxt.toUpperCase();
        }

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
        System.out.println("#### rowcnt : " + rowcnt);
        String colName = "";
        for (int i = 0; i < colcnt; i++) {
            colName = tblData.getColumnName(i);
            if (colName.equals("AMOUNT")) {
                amtColumnNo = i;
                break;
            }
        }
        tot = 0;
        for (int i = 0; i < rowcnt; i++) {
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
        int rowno = CommonUtil.convertObjToInt(tblData.getSelectedRow());
        if (rowno != -1 && CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 26)).equals("Y")) {
            enableIntBranchButon(true);
            return;
        } else {
            enableIntBranchButon(false);
        }
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
            String trans_id = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 0));
//            String dt= CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 5));
            String bran = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 20));
            String act_num = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 3));
            String trans_type = CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 19));
            Date dt = (Date) tblData.getValueAt(tblData.getSelectedRow(), 5);
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

    //Added By Kannan AR
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            this.setClosable(false);//hide close button
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            chkUnAuth.setSelected(true);
            btnViewActionPerformed(null);
        }
        if(hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
            this.setClosable(false);//hide close button
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            chkUnAuth.setSelected(true);
            btnViewActionPerformed(null);
        }
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.resetByViewAllTrans();
        }
        if(fromNewAuthorizeUI){
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.resetByViewAllTrans();
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintActionPerformed
        // TODO add your handling code here:
        if (tblData.getSelectedRow() == -1) {
            ClientUtil.showAlertWindow("Please select a row in the table grid");
            return;
        }
        System.out.println("tblData.getValueAt(tblData.getSelectedRow(),1)" + tblData.getValueAt(tblData.getSelectedRow(), 19));
        if ((tblData.getValueAt(tblData.getSelectedRow(), 24) == null)) {
            ClientUtil.displayAlert("cannot produce print for this record!!!!!");
            return;
        }
        //added by anju lakshmi for print option 15/10/2014
        if ((CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 19)) != null && CommonUtil.convertObjToStr(tblData.getValueAt(tblData.getSelectedRow(), 19)).equals("TRANSFER"))) {

            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            paramMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 23));
            paramMap.put("TransDt", (tblData.getValueAt(tblData.getSelectedRow(), 5)));
            ttIntgration.setParam(paramMap);
            if ((tblData.getValueAt(tblData.getSelectedRow(), 0).equals("MDS"))) {
                paramMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 0));
                ttIntgration.integrationForPrint("MDSReceiptsTransfer");
            } else {
                ttIntgration.integrationForPrint("ReceiptPayment");
            }
       } else {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            paramMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 23));
            paramMap.put("TransDt", (tblData.getValueAt(tblData.getSelectedRow(), 5)));
            ttIntgration.setParam(paramMap);
            if (tblData.getValueAt(tblData.getSelectedRow(), 24).equals("MDS")) {
                paramMap.put("TransId", tblData.getValueAt(tblData.getSelectedRow(), 0));
                ttIntgration.integrationForPrint("MDSReceipts");
            } else if (tblData.getValueAt(tblData.getSelectedRow(), 6).equals("CREDIT")) {
                ttIntgration.integrationForPrint("CashReceipt");
            } else {
                ttIntgration.integrationForPrint("CashPayment");
            }
        }

    }//GEN-LAST:event_btnReprintActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
       
    }//GEN-LAST:event_tblDataMouseClicked

    private void enableIntBranchButon(boolean flag) {
        ViewInterBranch.setEnabled(flag);
    }
    
    private void ViewInterBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewInterBranchActionPerformed
        // TODO add your handling code here:
        int rowno = CommonUtil.convertObjToInt(tblData.getSelectedRow());
        if (rowno < 0) {
            ClientUtil.displayAlert("Please Select Row first");
            return;
        }
        if (rowno != -1 && CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 26)).equals("Y") /*&&
                 CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 16)).equals("AUTHORIZED")*/) {
            final HashMap datamap = new HashMap();
            if(CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 1)).length() > 0){
               datamap.put("BATCH_ID", CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 1)));
            }else{
              datamap.put("BATCH_ID", CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 0)));
            }
            datamap.put("TRANS_DT", getProperFormatDate(tblData.getValueAt(rowno, 5)));
            datamap.put("INITIATED_BRANCH", CommonUtil.convertObjToStr(tblData.getValueAt(rowno, 21)));
            datamap.put("RESIZE", "RESIZE");
            System.out.println("InterBranchYes" + datamap);
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws InterruptedException, Exception /**
                 * Execute some operation
                 */
                {
                    TableDialogUI tableDialogUI = null;
                    tableDialogUI = new TableDialogUI("showInterbranchTransaction", datamap);
                    tableDialogUI.setTitle("Inter Branch Transaction details");
                    tableDialogUI.show();
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
            } //Progress bar code ends here                  
        } else {
        }
    }//GEN-LAST:event_ViewInterBranchActionPerformed

    public Date getProperFormatDate(Object obj) {
        Date dt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            dt = (Date) currDt.clone();
            dt.setDate(tempDt.getDate());
            dt.setMonth(tempDt.getMonth());
            dt.setYear(tempDt.getYear());
        }
        return dt;
    }
    
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
    private com.see.truetransact.uicomponent.CButton ViewInterBranch;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnReprint;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboAcHdID;
    private com.see.truetransact.uicomponent.CComboBox cboBranchID;
    private com.see.truetransact.uicomponent.CComboBox cboDate;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
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
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSearch;
    private com.see.truetransact.uicomponent.CLabel lblSelTrans;
    private com.see.truetransact.uicomponent.CLabel lblSelTransValue;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
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
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtNarration;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private javax.swing.JTextField txtSearchData;
    // End of variables declaration//GEN-END:variables
}

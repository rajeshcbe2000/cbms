/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PrintUI.java
 *
 * Created on August 24, 2003, 1:46 PM
 */

package com.see.truetransact.ui.remittance.printingdetails;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.LinkedHashMap;

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
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.common.tools.treebuilder.util.ParameterBean;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.supporting.InventoryMovement.InventoryMovementUI;

/**
 *
 */
public class PrintUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
//    private final ViewAllRB resourceBundle = new ViewAllRB();
    private PrintOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    CPanel panelParent = null;
    int amtColumnNo=0;
    double tot = 0;
    boolean flag = false;
    boolean check= true;
    
    private final static Logger log = Logger.getLogger(PrintUI.class);
    
    /** Creates new form ViewAll */
    public PrintUI() {
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        initComponents();
        internationalize();
        setObservable();
        initComponentData();
        toFront();
//        populateData();
        panMultiSearch.setVisible(true);
        if (parent != null) {
            parent.toFront();
            setTitle("List for " + parent.getTitle());
        }
        btnClear.setVisible(false);
        chkSelectAll.setVisible(true);
        panSearch.setVisible(false);
    }
    
    private void initComponentData() {
        cboProductId.setModel(observable.getCbmProductId());
    }
    
    private void setupScreen() {

        
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
            observable = new PrintOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    
    
  
    public void show() {
        if (observable.isAvailable()) {
            super.show();
        }
    }
    

    
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
        
    public void populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getPrintingDetails");
        whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        whereMap.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
                whereMap.put("FROM_DT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                whereMap.put("TO_DT", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
               if(flag == false)
                {
                  whereMap.put("AUTHORIZECHECK","");   
                }
//        System.out.println("#### where map : "+whereMap);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        flag = false;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblNextInstNo = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        btnPrinted = new com.see.truetransact.uicomponent.CButton();
        btnNotPrinted = new com.see.truetransact.uicomponent.CButton();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        txtfavouring = new com.see.truetransact.uicomponent.CTextField();
        lblFavouring = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtNo = new com.see.truetransact.uicomponent.CTextField();
        lblno = new com.see.truetransact.uicomponent.CLabel();
        btnPrintReport = new com.see.truetransact.uicomponent.CButton();
        txtInstrumentNo = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnOk = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        lblSelTrans = new com.see.truetransact.uicomponent.CLabel();
        lblAllTrans = new com.see.truetransact.uicomponent.CLabel();
        lblSelTransValue = new com.see.truetransact.uicomponent.CLabel();
        lblAllTransValue = new com.see.truetransact.uicomponent.CLabel();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(825, 650));
        setPreferredSize(new java.awt.Dimension(825, 650));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(800, 160));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(800, 160));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(new javax.swing.border.EtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(700, 160));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(700, 160));
        lblProductId.setText("Product ID");
        lblProductId.setMaximumSize(new java.awt.Dimension(75, 18));
        lblProductId.setMinimumSize(new java.awt.Dimension(75, 18));
        lblProductId.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 25);
        panMultiSearch.add(lblProductId, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 1);
        panMultiSearch.add(cboProductId, gridBagConstraints);

        lblNextInstNo.setText("Next Instrument No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panMultiSearch.add(lblNextInstNo, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        panMultiSearch.add(lblFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        panMultiSearch.add(lblToDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panMultiSearch.add(tdtFromDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 28);
        panMultiSearch.add(tdtToDate, gridBagConstraints);

        btnPrinted.setText("Printed");
        btnPrinted.setMaximumSize(new java.awt.Dimension(75, 23));
        btnPrinted.setMinimumSize(new java.awt.Dimension(75, 23));
        btnPrinted.setPreferredSize(new java.awt.Dimension(75, 23));
        btnPrinted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintedActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(btnPrinted, gridBagConstraints);

        btnNotPrinted.setText("Not Printed");
        btnNotPrinted.setMinimumSize(new java.awt.Dimension(99, 23));
        btnNotPrinted.setPreferredSize(new java.awt.Dimension(99, 23));
        btnNotPrinted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotPrintedActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(btnNotPrinted, gridBagConstraints);

        chkSelectAll.setText("Select All");
        chkSelectAll.setMaximumSize(new java.awt.Dimension(81, 23));
        chkSelectAll.setPreferredSize(new java.awt.Dimension(81, 23));
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panMultiSearch.add(chkSelectAll, gridBagConstraints);

        txtfavouring.setMinimumSize(new java.awt.Dimension(225, 21));
        txtfavouring.setPreferredSize(new java.awt.Dimension(225, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(txtfavouring, gridBagConstraints);

        lblFavouring.setText("Favouring");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblFavouring, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMultiSearch.add(txtAmount, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblAmount, gridBagConstraints);

        txtNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(txtNo, gridBagConstraints);

        lblno.setText("no");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblno, gridBagConstraints);

        btnPrintReport.setText("Print Report");
        btnPrintReport.setMinimumSize(new java.awt.Dimension(110, 23));
        btnPrintReport.setPreferredSize(new java.awt.Dimension(110, 23));
        btnPrintReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintReportActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(btnPrintReport, gridBagConstraints);

        txtInstrumentNo.setMinimumSize(new java.awt.Dimension(25, 21));
        txtInstrumentNo.setPreferredSize(new java.awt.Dimension(25, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 101;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 3, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMultiSearch.add(txtInstrumentNo, gridBagConstraints);

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(25, 21));
        txtInstrumentNo2.setPreferredSize(new java.awt.Dimension(25, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 74;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMultiSearch.add(txtInstrumentNo2, gridBagConstraints);

        panSearchCondition.add(panMultiSearch, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setLayout(new java.awt.GridBagLayout());

        srcTable.setPreferredSize(new java.awt.Dimension(454, 175));
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
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblDataMouseDragged(evt);
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

        panSearch.setLayout(new java.awt.GridBagLayout());

        panSearch.setMinimumSize(new java.awt.Dimension(823, 40));
        panSearch.setPreferredSize(new java.awt.Dimension(823, 40));
        btnOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnOk, gridBagConstraints);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnCancel, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
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

        lblSelTrans.setText("Selected Transactions Total ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panSearch.add(lblSelTrans, gridBagConstraints);

        lblAllTrans.setText("All Transactions Total ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
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
        gridBagConstraints.gridx = 4;
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
    }//GEN-END:initComponents

    private void btnPrintReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintReportActionPerformed
        // TODO add your handling code here:
        int x = 0;
        int y = 0;
        boolean missingMap = false;
         boolean ins= false;
        LinkedHashMap paramMaps = new LinkedHashMap();
        EnhancedTableModel td = (EnhancedTableModel) tblData.getModel();
        ArrayList arrlist = td.getDataArrayList();
        ArrayList subarray = new ArrayList();
        if(tblData.getRowCount()>0) {
            for(int i=0; i< tblData.getRowCount(); i++) {
                //                subarray = tblData.getValueAt(i,0);
                boolean column = new Boolean(CommonUtil.convertObjToStr(tblData.getValueAt(i,0))).booleanValue();
                if(column) {
                    txtAmount.setText(CommonUtil.convertObjToStr(tblData.getValueAt(i,7)));
                    txtfavouring.setText(CommonUtil.convertObjToStr(tblData.getValueAt(i,6)));
                    HashMap map= new HashMap();
                    map.put("BEHAVES_LIKE", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
                    map.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                    List lst = ClientUtil.executeQuery("GetStartSlNo", map);
                    map = null;
                    if(lst.size()>0) {
                        boolean flag =false;
                        for(int j=0; j<lst.size(); j++)
                        {
                            if(ins==true) {
                                break;
                            }
                            flag = true;
                        map = (HashMap) (lst.get(j));
                         HashMap insIssue1 = new HashMap();
                            insIssue1 =(HashMap)lst.get(j);
                            insIssue1.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                            insIssue1.put("INS1",insIssue1.get("INSTRUMENT_PREFIX"));
                         List list3 = ClientUtil.executeQuery("getMaxInsNo", insIssue1);
                          HashMap maps = new HashMap();
                          HashMap hash = (HashMap)list3.get(0);
                         if(list3!=null && list3.size()>0 &&(hash.get("INS2")!=null)) {
                                
                                maps=(HashMap)list3.get(0);
                                if (CommonUtil.convertObjToInt(maps.get("INS2"))>=(CommonUtil.convertObjToInt(insIssue1.get("LEAVES_SLNO_TO")))) {
                                   flag = false;
                                    continue;
                                }
                                else{
                                   int num = CommonUtil.convertObjToInt(hash.get("INS2"));
                                   num++;
                                    map.put("MAX(INS2)",String.valueOf(num));
                                
                        while(x==y) {
                           map.put("INS1",insIssue1.get("INSTRUMENT_PREFIX"));
                            map.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                            List lists = ClientUtil.executeQuery("checkForMissingInstruments", map);
                            HashMap where1 = new HashMap();
                            where1=(HashMap)lists.get(0);
                            x= CommonUtil.convertObjToInt(where1.get("CNT"));
                            if(x==y) {
                                x=1;
                                ins=true;
                                break;
                            }
                            else{
                                int tk = (CommonUtil.convertObjToInt(map.get("MAX(INS2)")));
                                tk++;
                                List list1 = ClientUtil.executeQuery("checkForInventory", map);
                                HashMap map1 = new HashMap();
                                map1 = (HashMap) (list1.get(0));
                                int ten=(CommonUtil.convertObjToInt(map1.get("LEAVES_SLNO_TO")));
                                if(tk>ten) {
                                    ClientUtil.displayAlert("Instrument Number Is Not Available.");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                                map.put("MAX(INS2)",String.valueOf(tk));
                                map.put("INS1",map.get("INS1"));
                                x=0;
                            }
                        }
                                }
                         }else{
                             //else for if(list3!=null && list3.size()>0)
                               while(x==y) {
                           map.put("INS1",insIssue1.get("INSTRUMENT_PREFIX"));
                           map.put("MAX(INS2)",CommonUtil.convertObjToStr(insIssue1.get("LEAVES_SLNO_FROM")));
                            map.put("BRANCH_CODE",ProxyParameters.BRANCH_ID);
                            List lists = ClientUtil.executeQuery("checkForMissingInstruments", map);
                            HashMap where1 = new HashMap();
                            where1=(HashMap)lists.get(0);
                            x= CommonUtil.convertObjToInt(where1.get("CNT"));
                            if(x==y) {
                                x=1;
                                ins=true;
                                break;
                            }
                            else{
                                int tk = (CommonUtil.convertObjToInt(map.get("MAX(INS2)")));
                                tk++;
                                List list1 = ClientUtil.executeQuery("checkForInventory", map);
                                HashMap map1 = new HashMap();
                                map1 = (HashMap) (list1.get(0));
                                int ten=(CommonUtil.convertObjToInt(map1.get("LEAVES_SLNO_TO")));
                                if(tk>ten) {
                                    ClientUtil.displayAlert("Instrument Number Is Not Available.");
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                                map.put("MAX(INS2)",String.valueOf(tk));
                                map.put("INS1",map.get("INS1"));
                                x=0;
                            }
                        }
                         }
                        
                        }
                    if(flag == false){
                        ClientUtil.displayAlert("Instrument Number Is Not Available.");
                                btnCancelActionPerformed(null);
                                return;
                    }
                
                        if(check){
                            txtInstrumentNo.setText(CommonUtil.convertObjToStr(map.get("INS1")));
                            txtInstrumentNo2.setText(CommonUtil.convertObjToStr(map.get("MAX(INS2)")));
                        }
                    }
                    else{
                         ClientUtil.displayAlert("Instrument Number Is Not Available.");
                                btnCancelActionPerformed(null);
                                return;
                    }
                    check = true;
                    ParameterBean paramBean = new ParameterBean();
                    paramBean.setParamName("GroupId_RoleName_UserId");
                    paramBean.setParamValue("GRP00001System Administratorsysadmin");
                    paramBean.setParamType("M");
                    paramMaps.put("GroupId_RoleName_UserId", paramBean);
                    
                    String str = CommonUtil.convertObjToStr(tblData.getValueAt(i,2));
                    paramBean = new ParameterBean();
                    paramBean.setParamName("ISSUE_ID");
                    paramBean.setParamValue(str);
                    paramBean.setParamType("M");
                    paramBean.setDataType(12);
                    paramMaps.put("ISSUE_ID", paramBean);
                    
                    paramBean = new ParameterBean();
                    paramBean.setParamName("N0_OF_ROWS_PER_PAGE");
                    paramBean.setParamValue("1000");
                    paramBean.setParamType("M");
                    paramBean.setDataType(2);
                    paramMaps.put("N0_OF_ROWS_PER_PAGE", paramBean);
                    System.out.println("#$#$# Parameters : "+paramMaps);
//                    TTIntegration.integration("PayOrder", paramMaps);
                    
                    int a = ClientUtil.confirmationAlert("Do You Want To Update The Status?");
                    int b= 0;
                    if(b==a) {
                        map = (HashMap) lst.get(0);
                        HashMap where = new HashMap();
                        where.put("ISSUE_ID",CommonUtil.convertObjToStr(tblData.getValueAt(i,2)));
                        where.put("ISTRUMENT_NO1", txtInstrumentNo.getText());
                        where.put("ISTRUMENT_NO2", txtInstrumentNo2.getText());
                        ClientUtil.execute("updateRemitIssueForPrinting", where);
                        map = new HashMap();
                        map.put("ISSUE_ID",CommonUtil.convertObjToStr(tblData.getValueAt(i,2)));
                        map.put("STATUS_BY", TrueTransactMain.USER_ID);
                        ClientUtil.execute("updatePayorderdd",  map);
                        tblData.setValueAt(txtInstrumentNo.getText(), i, 8);
                        tblData.setValueAt( txtInstrumentNo2.getText(), i, 9);
                        ins=false;
                    }
                    else{
                        
                        int c = ClientUtil.confirmationAlert("Do You Want To Exit Without Continuing Printing?");
                        int d= 0;
                        if(c!=d) {
                            String number1="";
                            String instno1 = "";
                            do{
                                number1 = COptionPane.showInputDialog(this,"Enter The Instrument No1 To Be Printed");
                                instno1 = (CommonUtil.convertObjToStr(map.get("INS1")));
                                if(!(number1.equals(instno1))) {
                                    ClientUtil.displayAlert("Instrument No1 should be of same series");
                                }
                            }while (!(number1.equals(instno1)));
                            int stNo;
                            int StrToInt;
                            String number2 = "";
                            do{
                                number2 = COptionPane.showInputDialog(this,"Enter The Instrument No2 To Be Printed");
                                stNo = CommonUtil.convertObjToInt(map.get("MAX(INS2)"));
                                StrToInt = CommonUtil.convertObjToInt(number2);
                                if (StrToInt<stNo) {
                                    ClientUtil.displayAlert("Newly Entered Instrument No2 cannot be less Than Current Instrument No 2");
                                }
                            }while(StrToInt<stNo);
                            stNo++;
                            if(StrToInt>=stNo) {
                                int startNo =CommonUtil.convertObjToInt(map.get("MAX(INS2)"));
                                int endNo = CommonUtil.convertObjToInt(number2);
                                endNo--;
                                HashMap where = new HashMap();
                                where.put("INSTRUMENT_START_NO1",number1);
                                where.put("INSTRUMENT_START_NO2",new Integer(startNo));
                                where.put("INSTRUMENT_END_NO1",number1);
                                where.put("INSTRUMENT_END_NO2",new Integer(endNo));
                                where.put("PROD_ID",((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
                                InventoryMovementUI InventoryMovement = new InventoryMovementUI(this);
                                com.see.truetransact.ui.TrueTransactMain.showScreen(InventoryMovement);
                                boolean print = InventoryMovement.missingInstrument(where);
                                break;
                            }
                            List list2 = ClientUtil.executeQuery("checkForInventory", map);
                            HashMap map1 = new HashMap();
                            map1 = (HashMap) (list2.get(0));
                            int ten=(CommonUtil.convertObjToInt(map1.get("LEAVES_SLNO_TO")));
                            if(stNo>ten) {
                                ClientUtil.displayAlert("Instrument Number Is Not Available.");
                                btnCancelActionPerformed(null);
                                return;
                            }
                            
                            txtInstrumentNo2.setText(number2);
                            txtInstrumentNo.setText(number1);
                            
                            check = false;
                            
                            i=i-1;
                            
                        }
                        else {
                            break;
                        }
                        
                    }
                }
            }
            
            txtAmount.setText("");
            txtfavouring.setText("");
            txtInstrumentNo.setText("");
            txtInstrumentNo2.setText("");
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getPrintingDetails");
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
            whereMap.put("FROM_DT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
            whereMap.put("TO_DT", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
            whereMap.put("AUTHORIZECHECK","");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                log.info("populateData...");
                ArrayList heading = observable.populateData(viewMap, tblData);
            } catch( Exception e ) {
                System.err.println( "Exception " + e.toString() + "Caught" );
                e.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_btnPrintReportActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
         observable.setSelectAll(new Boolean(chkSelectAll.isSelected()));
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnNotPrintedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotPrintedActionPerformed
        // TODO add your handling code here:
        chkSelectAll.setVisible(true);
        tblData.setEnabled(true);
        btnPrintReport.setEnabled(true);
            populateData();
    }//GEN-LAST:event_btnNotPrintedActionPerformed

    private void tblDataMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseDragged
        // TODO add your handling code here:
//        Point p = evt.getPoint();
//        tot = tot + Double.parseDouble(
//        tblData.getModel().getValueAt(
//        tblData.rowAtPoint(p),
//        tblData.columnAtPoint(p)).toString());
//        lblSelTransValue.setText(String.valueOf(tot));            
    }//GEN-LAST:event_tblDataMouseDragged

    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProductIdActionPerformed

    private void btnPrintedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintedActionPerformed
        // TODO add your handling code here:
        chkSelectAll.setVisible(false);
        tblData.setEnabled(false);
        flag = true;
        txtAmount.setText("");
        txtfavouring.setText("");
        btnPrintReport.setEnabled(false);
        populateData();
    }//GEN-LAST:event_btnPrintedActionPerformed

   
    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
       
        txtAmount.setText("");
        txtfavouring.setText("");
        int[] rows = tblData.getSelectedRows();
        int rowcnt = tblData.getSelectedRowCount();
        int j = tblData.getSelectedRow();
        for (int i=0; i<rowcnt; i++) {
            txtAmount.setText(CommonUtil.convertObjToStr(tblData.getValueAt(j,7)));
            txtfavouring.setText(CommonUtil.convertObjToStr(tblData.getValueAt(j,6)));
            }

    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
      
    }//GEN-LAST:event_tblDataMouseMoved

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        // Add your handling code here:
        whenTableRowSelected();
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        this.dispose();
        System.out.println("observable.fillData(-1) " + observable.fillData(-1));
        if (parent != null) ((CInternalFrame) parent).fillData(observable.fillData(-1));
        if (panelParent != null) ((CPanel) panelParent).fillData(observable.fillData(-1));
    }//GEN-LAST:event_btnClearActionPerformed
    
        private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }                            
        
        private void internationalize() {
          
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
//            new ViewAll(mapParam).show();
        }

        public void update(Observable o, Object arg) {

        }
        
        private boolean instrumentsAvailable(String no1, String no2){
           boolean  exists = false;
              HashMap map = new HashMap();
                HashMap where = new HashMap();
                where.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            map.put(CommonConstants.MAP_NAME, "ToCheckInstrumentIssueForManualEntry");
                where.put("INSTRUMENT_NO1",no1); 
           where.put("RECEIVED_BY", TrueTransactMain.USER_ID);
             map.put(CommonConstants.MAP_WHERE, where);
              ArrayList list = observable.getResultList(map);
               if(list != null){
                        if(list.size()!=0){
                            exists = configExists(list,no2);
                            return exists;
                        }
                    }
              return exists;
              
        }
        
      private boolean configExists(ArrayList resultList, String num2){
        boolean rangeExists = false;
        HashMap resultMap = (HashMap) resultList.get(0);
       int  configStartingNo = Integer.parseInt(CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_START_NO")));
        int configEndingNo = Integer.parseInt(CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT_END_NO")));
        int issueStartNo = CommonUtil.convertObjToInt(num2);
        if( (issueStartNo >= configStartingNo) && (issueStartNo <= configEndingNo) ){
            rangeExists = true;
        }
        return rangeExists;
    }
      
        
      

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnNotPrinted;
    private com.see.truetransact.uicomponent.CButton btnOk;
    private com.see.truetransact.uicomponent.CButton btnPrintReport;
    private com.see.truetransact.uicomponent.CButton btnPrinted;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAllTrans;
    private com.see.truetransact.uicomponent.CLabel lblAllTransValue;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblFavouring;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblNextInstNo;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSelTrans;
    private com.see.truetransact.uicomponent.CLabel lblSelTransValue;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblno;
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
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextField txtNo;
    private com.see.truetransact.uicomponent.CTextField txtfavouring;
    // End of variables declaration//GEN-END:variables
}


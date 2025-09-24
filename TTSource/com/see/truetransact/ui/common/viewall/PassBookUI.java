/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Pass Book.java
 * Swaroop 
 * Created on December 07, 2009, 1:46 PM
 */

package com.see.truetransact.ui.common.viewall;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.apache.log4j.Logger;

import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CLabel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.LinkedHashMap;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.see.truetransact.uicomponent.CButton;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.NumericValidation;

/**
 * @author  Swaroop
 */
public class PassBookUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer {
    private PassBookOB observable;
    HashMap  passMap= new HashMap();
    HashMap paramMap = null;
    int amtColumnNo=0;
    double tot = 0;
    String amtColName = "";
    String behavesLike = "";
    private LinkedHashMap Selected = new LinkedHashMap();
    private LinkedHashMap NotSelected = new LinkedHashMap();
     public static final boolean isFrameClosed = false;
    private final static Logger log = Logger.getLogger(PassBookUI.class);
    
    private TransDetailsUI transDetails = null;
    
    /** Creates new form PassBook */
    public PassBookUI() {
        setupInit();
        setupScreen();
    }
    
    
     public PassBookUI(String print) {
         setObservable();
         initComponents();
        txtAccNo.setAllowAll(true);
        txtLineNo.setValidation(new NumericValidation());
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
        txtLineNo.setValidation(new NumericValidation());
    }
    
    private void setupScreen() {
//        setModal(true);
        
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
            observable = PassBookOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void updateOBFields() {
        observable.setProdId(CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected()));
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
	observable.setTxtAccNo(txtAccNo.getText());
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
    }
    
    private void setCombos() {
        cboProdType.setModel(observable.getCbmProdType());
    }
    
    public void populateData() {
        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getAllTransactionsPassBook"+observable.getProdType());
        whereMap.put("FROM_SLNO", CommonUtil.convertObjToInt(txtLineNo.getText()));
        whereMap.put("ACT_NUM", observable.getTxtAccNo());
        if(observable.getTdtFromDate()!=null)
        whereMap.put("FROM_DT", observable.getTdtFromDate());
        else if(rdoPassBookType_RePrint.isSelected()){
             if(observable.getTdtFromDate()==null){
                 ClientUtil.displayAlert("Enter From And To Date");
                 return;
             }
        }
         if(observable.getTdtToDate()!=null)
        whereMap.put("TO_DT", observable.getTdtToDate());
         else if(observable.getTdtFromDate()!=null){
             java.util.Date dt = ClientUtil.getCurrentDateWithTime();
            whereMap.put("TO_DT", dt);
         }
        if(!rdoPassBookType_RePrint.isSelected())
            whereMap.put("REGULAR","REGULAR");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            heading = null;
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void show() {
        /* The following if condition commented by Rajesh
         * Because observable is not making null after closing the UI
         * So, if no data found in previously opened EnquiryUI instance
         * the observable.isAvailable() is false, so EnquiryUI won't open.
        */
//        if (observable.isAvailable()) {
            super.show();
//        }
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgAndOr = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPassBookType = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        panPassBookType = new com.see.truetransact.uicomponent.CPanel();
        rdoPassBookType_New = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPassBookType_Dup = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPassBookType_RePrint = new com.see.truetransact.uicomponent.CRadioButton();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblLineNo = new com.see.truetransact.uicomponent.CLabel();
        txtLineNo = new com.see.truetransact.uicomponent.CTextField();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(750, 675));
        setPreferredSize(new java.awt.Dimension(750, 675));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(700, 310));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(700, 310));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(450, 278));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(450, 278));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 15);
        panSearchCondition.add(lblProdType, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 15);
        panSearchCondition.add(cboProdType, gridBagConstraints);

        lblAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(lblAccNo, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panSearchCondition.add(panAcctNo, gridBagConstraints);

        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13));
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(175, 21));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(175, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 4, 6);
        panSearchCondition.add(lblAccNameValue, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 6);
        panSearchCondition.add(tdtToDate, gridBagConstraints);

        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panSearchCondition.add(btnPrint, gridBagConstraints);

        panPassBookType.setMinimumSize(new java.awt.Dimension(190, 23));
        panPassBookType.setPreferredSize(new java.awt.Dimension(140, 23));
        panPassBookType.setLayout(new java.awt.GridBagLayout());

        rdoPassBookType_New.setText("New");
        rdoPassBookType_New.setMaximumSize(new java.awt.Dimension(60, 27));
        rdoPassBookType_New.setMinimumSize(new java.awt.Dimension(60, 27));
        rdoPassBookType_New.setPreferredSize(new java.awt.Dimension(60, 27));
        rdoPassBookType_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPassBookType_NewActionPerformed(evt);
            }
        });
        panPassBookType.add(rdoPassBookType_New, new java.awt.GridBagConstraints());

        rdoPassBookType_Dup.setText("Duplicate");
        rdoPassBookType_Dup.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoPassBookType_Dup.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoPassBookType_Dup.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoPassBookType_Dup.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoPassBookType_Dup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPassBookType_DupActionPerformed(evt);
            }
        });
        panPassBookType.add(rdoPassBookType_Dup, new java.awt.GridBagConstraints());

        rdoPassBookType_RePrint.setText("Re-Print");
        rdoPassBookType_RePrint.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoPassBookType_RePrint.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoPassBookType_RePrint.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoPassBookType_RePrint.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoPassBookType_RePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPassBookType_RePrintActionPerformed(evt);
            }
        });
        panPassBookType.add(rdoPassBookType_RePrint, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 4, 4);
        panSearchCondition.add(panPassBookType, gridBagConstraints);

        btnView.setText("Transaction Details");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 4, 4);
        panSearchCondition.add(btnView, gridBagConstraints);

        lblLineNo.setText("Line No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 6);
        panSearchCondition.add(lblLineNo, gridBagConstraints);

        txtLineNo.setAllowAll(true);
        txtLineNo.setAllowNumber(true);
        txtLineNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLineNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLineNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearchCondition.add(txtLineNo, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(150);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 15);
        panSearchCondition.add(cboProductId, gridBagConstraints);

        lblProductId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panSearchCondition.add(lblProductId, gridBagConstraints);

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

        srcTable.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 14));
        srcTable.setPreferredSize(new java.awt.Dimension(452, 380));

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
        tblData.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13));
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tblDataMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDataKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDataKeyReleased(evt);
            }
        });
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
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

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
//        Object ob= new Boolean(true);
//        tblData.setValueAt(ob, tblData.getSelectedRow(), 0);
    }//GEN-LAST:event_tblDataMouseReleased

    private void tblDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyPressed
        // TODO add your handling code here:
         
    }//GEN-LAST:event_tblDataKeyPressed

    private void tblDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyReleased

    }//GEN-LAST:event_tblDataKeyReleased

    private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
        // TODO add your handling code here:
       
    }//GEN-LAST:event_tblDataFocusLost

    private void rdoPassBookType_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPassBookType_NewActionPerformed
        rdoPassBookType_Dup.setSelected(false);
        rdoPassBookType_RePrint.setSelected(false);
    }//GEN-LAST:event_rdoPassBookType_NewActionPerformed

    private void rdoPassBookType_DupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPassBookType_DupActionPerformed
        // TODO add your handling code here:
        rdoPassBookType_RePrint.setSelected(false);
        rdoPassBookType_New.setSelected(false);
    }//GEN-LAST:event_rdoPassBookType_DupActionPerformed

    private void rdoPassBookType_RePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPassBookType_RePrintActionPerformed
        // TODO add your handling code here:
        rdoPassBookType_Dup.setSelected(false);
        rdoPassBookType_New.setSelected(false);
    }//GEN-LAST:event_rdoPassBookType_RePrintActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.refreshTable();
        populateData();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        if(tblData.getRowCount()>0){
            if(!observable.getProdType().equals("TD")){
                HashMap passMap= new HashMap();
                if(rdoPassBookType_New.isSelected()){
                     String repName="PassBookNew";
                     passMap.put("Acct_Num",observable.getTxtAccNo());
                     callTTIntergration(repName,passMap);  
                     rdoPassBookType_New.setSelected(false);
                }else if(rdoPassBookType_Dup.isSelected()){
                    btnPrintActionPerformed();
                }else{
                    btnPrintActionPerformed();   
                }
            }else{
                if(rdoPassBookType_New.isSelected()){
                     String repName="RDPassBookNew";
                     passMap.put("ACT_NUM",observable.getTxtAccNo());
                     callTTIntergration(repName,passMap);  
                     rdoPassBookType_New.setSelected(false);
                }else if(rdoPassBookType_Dup.isSelected()){
                    RdPrintActionPerformed();   
                }else{
                    RdPrintActionPerformed();   
                }
            }
        }else{
            ClientUtil.displayAlert("No Data To Print");
        }
    }//GEN-LAST:event_btnPrintActionPerformed


    boolean btnIntDetPressed = false;
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : "+hash);
        txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));        
        observable.refreshTable();
        txtAccNoFocusLost(null);
        transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, txtAccNo.getText());
        //populateData();
    }
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        whereMap.put(CommonConstants.PRODUCT_TYPE, observable.getProdType());
        whereMap.put(CommonConstants.PRODUCT_ID, observable.getProdId());
        viewMap.put(CommonConstants.MAP_NAME, "PassBook.getAccountList");
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        
        if (txtAccNo.getText().length()>0) {
            observable.refreshTable();
            String actNum = txtAccNo.getText();
            observable.setTxtAccNo(txtAccNo.getText());
            transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
            observable.setAccountName();
            if (observable.getLblAccNameValue().length()<=0) {
                ClientUtil.displayAlert("Invalid Account No.");
                lblAccNameValue.setText("");
                txtAccNo.setText("");
            }else{
                lblAccNameValue.setText(observable.getLblAccNameValue());
                populateData();
                int lineNo = 0;
                HashMap passMap= new HashMap();
                passMap.put("ACT_NUM",observable.getTxtAccNo());
                java.util.List resultList;
                resultList = ClientUtil.executeQuery("getNextLineNoForPassBook"+observable.getProdType(),passMap);
                if(resultList != null && resultList.size()>0){
                    HashMap resultMap = (HashMap)resultList.get(0);
                    lineNo = CommonUtil.convertObjToInt(resultMap.get("LINE_NO"));
                    txtLineNo.setText(String.valueOf(lineNo));
                    if(observable.getProdType().equals("TD")){
                       txtLineNoFocusLost(null);
                    }
                    resultMap.clear();
                    resultMap = null;
                }
                resultList = null;
                passMap = null;
            }
        }
//        if (observable.getProdType().length()>0) {
//            observable.refreshTable();
//            String actNum = txtAccNo.getText();
//            observable.setTxtAccNo(txtAccNo.getText());
//            transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
//            observable.setAccountName();
//            lblAccNameValue.setText(observable.getLblAccNameValue());
//            if (observable.getLblAccNameValue().length()<=0) {
//                ClientUtil.displayAlert("Invalid Account No.");
//                lblAccNameValue.setText("");
//                txtAccNo.setText("");
//            }
//            populateData();
//        } else {
//            ClientUtil.displayAlert("Enter Product Type");
//            btnClearActionPerformed(null);
//            txtAccNo.setText("");
//        }
        
    }//GEN-LAST:event_txtAccNoFocusLost

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
//        observable.refreshTable();
//        String actNum = txtAccNo.getText();
//        observable.setTxtAccNo(txtAccNo.getText());
//        transDetails.setTransDetails(observable.getProdType(), ProxyParameters.BRANCH_ID, observable.getTxtAccNo());
//        observable.setAccountName();
//        if (observable.getLblAccNameValue().length()<=0) {
//            ClientUtil.displayAlert("Invalid Account No.");
//            lblAccNameValue.setText("");
//            txtAccNo.setText("");
//        }else{
//            lblAccNameValue.setText(observable.getLblAccNameValue());
//            populateData();
//        }
    }//GEN-LAST:event_txtAccNoActionPerformed

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        observable.setTdtToDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
        java.util.Date from = observable.getTdtFromDate();
        java.util.Date to = observable.getTdtToDate();
        if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
            displayAlert("To date should be greater than From Date...");
            tdtToDate.setDateValue("");
            tdtToDate.requestFocus();
        }
    }//GEN-LAST:event_tdtToDateFocusLost

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        observable.setTdtFromDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//        tdtToDate.setDateValue("");
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        
        System.out.println("#$#$ prodType : "+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
        observable.setProdType(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
        behavesLike = "";
        observable.setCbmProdId(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
        cboProductId.setModel(observable.getCbmProdId());
            
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
            
    
    private void tblDataMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseDragged
        // TODO add your handling code here:          
    }//GEN-LAST:event_tblDataMouseDragged


    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        // Add your handling code here:
        int rowcnt = tblData.getRowCount();
         int row = tblData.getSelectedRow();
        Object ob=new Boolean (false);
        for(int i=0;i<rowcnt;i++){
          tblData.setValueAt(ob, i, 0);   
        }
         ob=new Boolean (true);
        for (int i=row; i<rowcnt; i++) {
           tblData.setValueAt(ob, i, 0);          
        }
        tblData.setValueAt(ob, tblData.getSelectedRow(), 0); 

    }//GEN-LAST:event_tblDataMousePressed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        // Add your handling code here:
    }//GEN-LAST:event_tblDataMouseMoved

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
        transDetails.setTransDetails(null,null,null);
        lblAccNameValue.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
// TODO add your handling code here:
    System.out.println("#$#$ ProdId : "+((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
    observable.setProdId(CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected()));
    String prodType = (CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
    if (!(prodType.equals("GL") || prodType.equals("AB"))) {  
        String prodId = (CommonUtil.convertObjToStr(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected()));
        txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
    }
}//GEN-LAST:event_cboProductIdActionPerformed

private void txtLineNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLineNoFocusLost
// TODO add your handling code here:
    if(txtLineNo.getText()!=null && txtLineNo.getText().length()>0){
        populateData();
        int count = CommonUtil.convertObjToInt(txtLineNo.getText());
//        if(tblData.getRowCount()>0){
//            for(int i=0;i<count;i++){
//                tblData.setValueAt(new Boolean(true), i, 0);
//            }
//        }
    }
}//GEN-LAST:event_txtLineNoFocusLost
    
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
//            HashMap mapParam = new HashMap();
//
//            HashMap where = new HashMap();
//            where.put("beh", "CA");
//
//            mapParam.put("MAPNAME", "getSelectInwardClearingAuthorizeTOList");
            //mapParam.put("WHERE", where);
            // HashMap rMap = (HashMap) dao.getData(mapParam);

            // HashMap testMap = new HashMap();
            //testMap.put("MAPNAME", "getSelectOperativeAcctProductTOList");
            new PassBookUI().show();
        }

        public void update(Observable observed, Object arg) {
            ((ComboBoxModel)cboProdType.getModel()).setKeyForSelected(observable.getProdType());
            txtAccNo.setText(observable.getTxtAccNo());
            tdtFromDate.setDateValue(DateUtil.getStringDate(observable.getTdtFromDate()));
            tdtToDate.setDateValue(DateUtil.getStringDate(observable.getTdtToDate()));
        }
        public void callTTIntergration(String repName,HashMap parMap){
            System.out.println("Here is the param map :: " + parMap);
            TTIntegration ttIntgration = null;
            ttIntgration.setParam(parMap);
            ttIntgration.integrationForPrint(repName);
        }
        
        public  void alertMsg(){
            int lastLineNo = 0;
            if(observable.getPrintMap() != null){
                System.out.println("getPrintMap :"+observable.getPrintMap());
                passMap = observable.getPrintMap();
            }
            System.out.println("passMap"+passMap);
            if (passMap.containsKey("LAST_SLNO")) {
                lastLineNo = CommonUtil.convertObjToInt(passMap.get("LAST_SLNO"));
            }
            final HashMap nextLineNo = new HashMap();
            nextLineNo.put("NEXT_LINE_NO", new Integer(lastLineNo+1));
            StringBuffer sb = new StringBuffer().append("Last printed line no is ")
            .append(String.valueOf(lastLineNo) +
            ". So, next line no will be " + String.valueOf(lastLineNo+1) +
            "\nPress Yes to continue updating this next line no." +
            "\nPress No to enter new next line no");
            int a = ClientUtil.confirmationAlert(sb.toString());
            if(a==0){
                // do nothing
            } else{
                final CDialog passBookFrame = new CDialog(this, true);
                passBookFrame.setTitle("Passbook print Confirmation");
                passBookFrame.setSize(400, 200);
                passBookFrame.setLayout(null);
                passBookFrame.setDefaultCloseOperation(CDialog.DISPOSE_ON_CLOSE);
                
                StringBuffer sbMsg = new StringBuffer().append("<html><font color = red>").
                append("!!!Kindly clear the printer memory and switch off the printer.\n").
                append("Switch on the printer after 10 secs.</font></html>");
                CLabel lblMessage = new CLabel();
                lblMessage.setText(sbMsg.toString());
                lblMessage.setBounds(30,15,320,40);
                passBookFrame.getContentPane().add(lblMessage);
                
                CLabel lblNextLineNo = new CLabel();
                lblNextLineNo.setText("Enter next line no");
                lblNextLineNo.setBounds(40,70,150,20);
                passBookFrame.getContentPane().add(lblNextLineNo);
                
                final CTextField txtNextLineNo = new CTextField();
                txtNextLineNo.setText(String.valueOf(lastLineNo+1));
                txtNextLineNo.setBounds(200,70,100,20);
                txtNextLineNo.setValidation(new NumericValidation());
                passBookFrame.getContentPane().add(txtNextLineNo);
                
                CButton btnOk = new CButton();
                btnOk.setText("OK");
                btnOk.setBounds(170,100,70,30);
                btnOk.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        nextLineNo.put("NEXT_LINE_NO", new Integer(txtNextLineNo.getText()));
                        passBookFrame.dispose();
                    }
                });
                passBookFrame.getContentPane().add(btnOk);
                
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                /* Center frame on the screen */
                Dimension frameSize = passBookFrame.getSize();
                passBookFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
                frameSize = null;
                screenSize = null;
                //Display the window.
                passBookFrame.setVisible(true);
                //            passBookFrame.addWindowListener(new WindowListener(){
                //                public void windowOpened(WindowEvent e) {
                //
                //                }
                //
                //                public void windowClosing(WindowEvent e) {
                //                    passBookFrame.setVisible(false);
                //                    passBookFrame.dispose();
                //                }
                //
                //                public void windowClosed(WindowEvent e) {
                //                    passBookFrame.setVisible(false);
                //                    passBookFrame.dispose();
                //                }
                //
                //                public void windowIconified(WindowEvent e) {
                //
                //                }
                //
                //                public void windowDeiconified(WindowEvent e) {
                //
                //                }
                //
                //                public void windowActivated(WindowEvent e) {
                //
                //                }
                //
                //                public void windowDeactivated(WindowEvent e) {
                //
                //                }
                //
                //            });
                //                System.out.println("@@@###PAssMAp"+passMap);
                ////                for(int i=0;i<tblData.getRowCount();i++){
                //                    ClientUtil.execute("deleteTempPassBook",passMap);
                ////                }
            }
            System.out.println("NEXT_LINE_NO "+nextLineNo.get("NEXT_LINE_NO"));
            txtLineNo.setText(CommonUtil.convertObjToStr(nextLineNo.get("NEXT_LINE_NO")));
//            passMap= new HashMap();
//            passMap.put("ACT_NUM",observable.getTxtAccNo());
            System.out.println("OBser"+observable.getSelectedBranchID());
            passMap.put("LINE_NO", nextLineNo.get("NEXT_LINE_NO"));
            if(observable.getTdtToDate()!=null)
                passMap.put("TODT", observable.getTdtToDate());
            else {
                java.util.Date dt = ClientUtil.getCurrentDateWithTime();
                passMap.put("TODT", dt);
            }
            ClientUtil.execute("updatePassBookFlag",passMap);
        //    ClientUtil.execute("deleteTempPassBook",passMap);
            System.out.println("observable.getProdType()"+observable.getProdType());
            if(observable.getProdType().equals("OA")){
                ClientUtil.execute("updatePassBookLineNo",passMap);
            }
            else if(observable.getProdType().equals("AD")){
                ClientUtil.execute("updatePassBookLineNoAD",passMap);
            }
            observable.refreshTable();
            observable.clearPrintMap();
            
        }
        
        public void RdPrintActionPerformed(){
            if(rdoPassBookType_RePrint.isSelected()){
                
            }else if(rdoPassBookType_Dup.isSelected()){
                
            }else{
                String repName="RDPassBook";
                HashMap reportMap = new HashMap();
                reportMap.put("Acct_Num",CommonUtil.convertObjToStr(txtAccNo.getText().substring(0, 13)));
                reportMap.put("Sl_No",CommonUtil.convertObjToInt(txtLineNo.getText()));
                reportMap.put("Prod_Id",CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
                callTTIntergration(repName,reportMap);
                if(observable.getProdType().equals("TD")){
                    reportMap.put("Sl_No",CommonUtil.convertObjToInt(txtLineNo.getText())+(tblData.getRowCount()-1));
                    ClientUtil.execute("updatePassBookLineNoRD",reportMap);
                }
            }
            btnClearActionPerformed(null);
            
        }
                
        public void btnPrintActionPerformed(){
            Selected = new LinkedHashMap();
            NotSelected = new LinkedHashMap();
            boolean selected = false;
            int lineNo = 0;
             ClientUtil.execute("deleteTempPassBook1",passMap);
//            passMap.put("ACT_NUM",observable.getTxtAccNo());
//            final java.util.List resultList;
//            resultList = ClientUtil.executeQuery("getNextLineNoForPassBook"+observable.getProdType(),passMap);
//            if(resultList != null && resultList.size()>0){
//                final HashMap resultMap = (HashMap)resultList.get(0);
//                lineNo = CommonUtil.convertObjToInt(resultMap.get("LINE_NO"));
//            }
            lineNo = CommonUtil.convertObjToInt(txtLineNo.getText());
            for(int i=1;i<lineNo;i++){
                passMap = new HashMap();
                passMap.put("ACT_NUM",observable.getTxtAccNo());
                passMap.put("Acct_Num",observable.getTxtAccNo());
                passMap.put("TRANS_DT",null);
                passMap.put("PARTICULARS",null);
                passMap.put("INST_TYPE",null);
                passMap.put("INST_NO",null);
                passMap.put("INST_DT",null);
                passMap.put("DEBIT",null);
                passMap.put("CREDIT",null);
                passMap.put("BALANCE",null);
                passMap.put("SLNO",null);
                passMap.put("PAGENO",null);
                Object obj = new Integer(i);
                NotSelected.put(obj,passMap);
                Selected.put(obj,passMap);
                passMap=null;
            }
            System.out.println("@@@### Selected 1 : "+Selected);
            int slNo = 0;
            String prevBal = "0";
            for(int i=0;i<tblData.getRowCount();i++){
                passMap = new HashMap();
                boolean column = new Boolean(CommonUtil.convertObjToStr(tblData.getValueAt(i,0))).booleanValue();
                passMap.put("ACT_NUM",observable.getTxtAccNo());
                passMap.put("Acct_Num",observable.getTxtAccNo());
                passMap.put("TRANS_DT",CommonUtil.convertObjToStr(tblData.getValueAt(i,1)));
                passMap.put("PARTICULARS",CommonUtil.convertObjToStr(tblData.getValueAt(i,2)));
                passMap.put("INST_TYPE",CommonUtil.convertObjToStr(tblData.getValueAt(i,3)));
                passMap.put("INST_NO",CommonUtil.convertObjToStr(tblData.getValueAt(i,4)));
                passMap.put("INST_DT",CommonUtil.convertObjToStr(tblData.getValueAt(i,5)));
                passMap.put("DEBIT",CommonUtil.convertObjToDouble(tblData.getValueAt(i,6)));
                passMap.put("CREDIT",CommonUtil.convertObjToDouble(tblData.getValueAt(i,7)));
                passMap.put("BALANCE",CommonUtil.convertObjToDouble(tblData.getValueAt(i,8)));
//                if (i==0) {
//                    passMap.put("SLNO",CommonUtil.convertObjToStr(new Integer(lineNo)));
//                    passMap.put("SLNO",CommonUtil.convertObjToStr(new Integer(0)));  // for the first record i should be zero
                                                                                     // based on this only generating slnos in report
//                } else {
                    passMap.put("SLNO",CommonUtil.convertObjToInt(tblData.getValueAt(i,9)));
//                }
                lineNo = CommonUtil.convertObjToInt(tblData.getValueAt(i,9));
                passMap.put("PAGENO",CommonUtil.convertObjToInt(tblData.getValueAt(i,10)));
                passMap.put("TRANS_ID",CommonUtil.convertObjToStr(tblData.getValueAt(i,11)));
                passMap.put("BATCH_ID",CommonUtil.convertObjToStr(tblData.getValueAt(i,12)));
                
//                HashMap prevBalMap= new HashMap();
//                prevBalMap.put("ACT_NUM",observable.getTxtAccNo());
//                prevBalMap.put("SLNO",tblData.getValueAt(i,9));
//                prevBalMap.put("PAGENO",tblData.getValueAt(i,10));
//                
//                if (i==0) {
//                    java.util.List resultList;
//                    resultList = ClientUtil.executeQuery("getPassBookPrevBalance",passMap);
//                    if(resultList != null && resultList.size()>0){
//                        HashMap resultMap = (HashMap)resultList.get(0);
//                        prevBal = CommonUtil.convertObjToStr(resultMap.get("BALANCE"));
//                        resultMap.clear();
//                        resultMap = null;
//                    }
//                    resultList.clear();
//                    resultList = null;
//                }
                passMap.put("PREV_BAL", prevBal);
                
                Object obj = tblData.getValueAt(i, 10)+"."+ tblData.getValueAt(i, 9);
                if(!column)
                    NotSelected.put(obj,passMap);
                else {
                    selected = true;
                    Selected.put(obj,passMap);
                }
                prevBal = CommonUtil.convertObjToStr(tblData.getValueAt(i,8));
                passMap=null;
            }
            if (!selected) {
                Selected.clear();
            }
            
            System.out.println("@@@### Selected 2 : "+Selected);
            String accNo = CommonUtil.convertObjToStr(txtAccNo.getText());
            System.out.println("accNNN "+accNo);
            ArrayList addList =new ArrayList();
            if(Selected.isEmpty())
                addList =new ArrayList(NotSelected.keySet());
            else
                addList =new ArrayList(Selected.keySet());
            System.out.println("@@@### addList : "+addList);
            for (int i = 0; i < addList.size(); i++) {
                if (Selected.isEmpty()) {
                    passMap = (HashMap) NotSelected.get(addList.get(i));
                } else {
                    passMap = (HashMap) Selected.get(addList.get(i));
                }
                ClientUtil.execute("inserIntoTmpPassBook", passMap);
            }
            if (CommonUtil.convertObjToInt(txtLineNo.getText()) >= 1 && CommonUtil.convertObjToInt(txtLineNo.getText()) <= addList.size()) {
                HashMap map = new HashMap();
                System.out.println("inside if.!");
                if(passMap.get("ACT_NUM") != null && passMap.containsKey("ACT_NUM")){
                map.put("ACT_NUM", CommonUtil.convertObjToStr(passMap.get("ACT_NUM")));
                ClientUtil.execute("inserIntoTmpPassBook", map);
                }
            }
            String repName="PassBook";
           callTTIntergration(repName,passMap);
           observable.setPrintMap(passMap);
            //if(b==1)
           // {
            
        }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblLineNo;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panPassBookType;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAndOr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPassBookType;
    private com.see.truetransact.uicomponent.CRadioButton rdoPassBookType_Dup;
    private com.see.truetransact.uicomponent.CRadioButton rdoPassBookType_New;
    private com.see.truetransact.uicomponent.CRadioButton rdoPassBookType_RePrint;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtLineNo;
    // End of variables declaration//GEN-END:variables
}

